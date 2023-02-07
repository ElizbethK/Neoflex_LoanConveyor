package ru.neostudy.loanConveyorProject.deal.controller;


import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.neostudy.loanConveyorProject.conveyor.controller.ConveyorController;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.FinishRegistrationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;
import ru.neostudy.loanConveyorProject.deal.KafkaProducer;
import ru.neostudy.loanConveyorProject.deal.config.KafkaTopicsConfig;
import ru.neostudy.loanConveyorProject.deal.dto.EmailMessage;
import ru.neostudy.loanConveyorProject.deal.dto.Theme;
import ru.neostudy.loanConveyorProject.deal.entity.Application;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.exception.ResourceNotFoundException;
import ru.neostudy.loanConveyorProject.deal.service.ApplicationService;
import ru.neostudy.loanConveyorProject.deal.service.ClientService;
import ru.neostudy.loanConveyorProject.deal.service.ScoringDataDTOService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/deal")
public class DealController {
    private static final Logger logger = LoggerFactory.getLogger(DealController.class);


    @Autowired
    ClientService clientService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    ScoringDataDTOService scoringDataDTOService;

    @Autowired
    private FeignDealClient feignDealClient;

    @Autowired
    KafkaTopicsConfig topicsConfig;

    @Autowired
    KafkaProducer kafkaProducer;


    public DealController(ClientService clientService, ApplicationService applicationService, ScoringDataDTOService scoringDataDTOService) {
        this.clientService = clientService;
        this.applicationService = applicationService;
        this.scoringDataDTOService = scoringDataDTOService;
    }


   /*
    POST: /deal/application - расчёт возможных условий кредита.
    Request - LoanApplicationRequestDTO, response - List<LoanOfferDTO>*/

    @PostMapping("/application")
    public List<LoanOfferDTO> createClientAndApp(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO,
                                                BindingResult bindingResult){
        logger.info("DealController. Запущен метод createClientAndApp с {}", loanApplicationRequestDTO);

        Client client = clientService.createClient(loanApplicationRequestDTO);
        Application application = applicationService.createApplication(client);
        
        List<LoanOfferDTO> loanOfferDTOList = feignDealClient.getPossibleOffers(loanApplicationRequestDTO);

        logger.info("DealController. Завершен createClientAndApp. Создан {}, {}", client, application);
        return applicationService.setIDForEachOffer(loanOfferDTOList, application);
    }


   /*
    PUT: /deal/offer - Выбор одного из предложений.
    Request LoanOfferDTO, response void.*/

    @PutMapping("/offer")
    public void chooseOffer(@RequestBody LoanOfferDTO loanOfferDTO) throws ResourceNotFoundException {
        logger.info("DealController. Запущен метод chooseOffer с {}", loanOfferDTO);
        Application updatedApplication = applicationService.updateApplication(loanOfferDTO);
        logger.info("DealController. Запущен метод updateApplication");

        NewTopic finishRegistrationTopic = topicsConfig.doFinishRegistrationTopic();
        kafkaProducer.sendMessageToTopic(finishRegistrationTopic,
                "Finish registration");
        logger.info("Kafka сообщение о завершении регистрации отправлено");

        EmailMessage emailMessage = new EmailMessage(
                updatedApplication.getClient().getEmail(), Theme.FINISH_REGISTRATION,
                updatedApplication.getApplicationId(), "Hello! Your loan application №"
                + loanOfferDTO.getApplicationId()
                + " successfully approved! Please, finish your registration by the link: " +
                "http://localhost:8080/deal/calculate/{applicationId}"
        );
        feignDealClient.sendEmail(emailMessage);
    }


   /*
    PUT: /deal/calculate/{applicationId} - завершение регистрации + полный подсчёт кредита.
    Request - FinishRegistrationRequestDTO, param - Long,  response void.
    */
    @PutMapping("/calculate/{applicationId}")
    public void finishRegistration(@PathVariable(value = "applicationId") Long id,
                                   @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                   BindingResult bindingResult) throws ResourceNotFoundException {
        logger.info("DealController. Запущен метод finishRegistration с {}, {}", id, finishRegistrationRequestDTO);
        logger.info("DealController. Поиск application по id");
        Optional <Application> optionalApplication = Optional.ofNullable(applicationService.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Application with id " + id + " is not found")));
        Application application = optionalApplication.get();

        logger.info("DealController. Вызов метода consolidateScoringInformation");
        ScoringDataDTO scoringDataDTO = scoringDataDTOService.consolidateScoringInformation(application,finishRegistrationRequestDTO);

        feignDealClient.getCalculatedCredit(scoringDataDTO);
        logger.info("DealController. Переход на ConveyorController. (метод getCalculatedCredit)");

        requestDocuments(id);
        logger.info("Kafka сообщение об оформлении документов отправлено");
    }


    //-----------------------------------------------------------------------------------------------------------//

    //POST: /deal/document/{applicationId}/send - запрос на отправку документов
    @PostMapping("/document/{applicationId}/send")
    public void requestDocuments(@PathVariable (value = "applicationId")Long applicationId) throws ResourceNotFoundException {
       NewTopic createDocumentsTopic = topicsConfig.doCreateDocumentsTopic();
       kafkaProducer.sendMessageToTopic(createDocumentsTopic, "Go to creating documents");

       Application application = getApplicationById(applicationId);

       EmailMessage emailMessage = new EmailMessage(
                application.getClient().getEmail(), Theme.CREATE_DOCUMENTS,
                application.getApplicationId(), "Hello! Your loan application №"
                + application.getApplicationId()
                + " passed all checks! Please, send creating documents request by the link: " +
                "http://localhost:8080/deal/calculate/{applicationId}"
        );
        feignDealClient.sendEmail(emailMessage);




    }


    //POST: /deal/document/{applicationId}/sign - запрос на подписание документов
    @PostMapping("/document/{applicationId}/sign")
    public void requestSign(){

    }


    //POST: /deal/document/{applicationId}/code - подписание документов
    @PostMapping("/document/{applicationId}/code")
    public void requestSesCode(){
    }


    //-----------------------------------------------------------------------------------------------------------//
                                         // ---- ADMIN -----//
    //   GET: /deal/admin/application/{applicationId} - получить заявку по id
    @GetMapping("/admin/application/{applicationId}")
    public Application getApplicationById(@PathVariable(value = "applicationId") Long id) throws ResourceNotFoundException {
        logger.info("DealController. Поиск application по id");
        Optional <Application> optionalApplication = Optional.ofNullable(applicationService.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Application with id " + id + " is not found")));

        Application application = optionalApplication.get();
        logger.info("DealController. Найден {}", application);
        return application;
    }


    //  GET: /deal/admin/application - получить все заявки
    @GetMapping("/admin/application")
    public List<Application> getListOfApplications(){
        List<Application> applicationList = applicationService.getAllApplications();
        return applicationList;
    }





}

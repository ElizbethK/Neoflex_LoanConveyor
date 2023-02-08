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
import ru.neostudy.loanConveyorProject.deal.entity.StatusHistoryJsonb;
import ru.neostudy.loanConveyorProject.deal.enums.ApplicationStatus;
import ru.neostudy.loanConveyorProject.deal.enums.ChangeType;
import ru.neostudy.loanConveyorProject.deal.exception.ResourceNotFoundException;
import ru.neostudy.loanConveyorProject.deal.service.ApplicationService;
import ru.neostudy.loanConveyorProject.deal.service.ClientService;
import ru.neostudy.loanConveyorProject.deal.service.ScoringDataDTOService;

import javax.validation.Valid;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
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
                "http://localhost:8081/deal/calculate/{applicationId}"
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
        application.setApplicationStatus(ApplicationStatus.CC_APPROVED);
        application.addToStatusHistoryList(new StatusHistoryJsonb(ApplicationStatus.CC_APPROVED,
                LocalDate.now(), ChangeType.AUTOMATIC));

        logger.info("DealController. Вызов метода consolidateScoringInformation");
        ScoringDataDTO scoringDataDTO = scoringDataDTOService.consolidateScoringInformation(application,finishRegistrationRequestDTO);

        feignDealClient.getCalculatedCredit(scoringDataDTO);
        logger.info("DealController. Переход на ConveyorController. (метод getCalculatedCredit)");

        NewTopic createDocumentsTopic = topicsConfig.doCreateDocumentsTopic();
        kafkaProducer.sendMessageToTopic(createDocumentsTopic, "Go to creating documents");
        logger.info("Kafka запрос на оформление документов отправлен");

        EmailMessage emailMessage = new EmailMessage(
                application.getClient().getEmail(), Theme.CREATE_DOCUMENTS,
                application.getApplicationId(), "Hello! Your loan application №"
                + application.getApplicationId()
                + " passed all checks! Please, send creating documents request by the link: " +
                "http://localhost:8081/deal/document/{applicationId}/send"
        );
        feignDealClient.sendEmail(emailMessage);
        application.setApplicationStatus(ApplicationStatus.PREPARE_DOCUMENTS);
        application.addToStatusHistoryList(new StatusHistoryJsonb(ApplicationStatus.PREPARE_DOCUMENTS,
                LocalDate.now(), ChangeType.AUTOMATIC));
    }


    //-----------------------------------------------------------------------------------------------------------//

    //POST: /deal/document/{applicationId}/send - запрос на отправку документов
    @PostMapping("/document/{applicationId}/send")
    public void requestDocuments(@PathVariable (value = "applicationId")Long applicationId) throws ResourceNotFoundException {
       NewTopic sendDocumentsTopic = topicsConfig.doSendDocumentsTopic();
       kafkaProducer.sendMessageToTopic(sendDocumentsTopic, "Go to signing documents");

       Application application = getApplicationById(applicationId);

  // Email message must be with attachment, created in "createAllDocuments" method
        createAllDocuments(application);

        application.setApplicationStatus(ApplicationStatus.DOCUMENT_CREATED);
        application.addToStatusHistoryList(new StatusHistoryJsonb(ApplicationStatus.DOCUMENT_CREATED,
                LocalDate.now(), ChangeType.AUTOMATIC));

        EmailMessage emailMessageWithAttachment = new EmailMessage(
                application.getClient().getEmail(), Theme.SEND_DOCUMENTS,
                application.getApplicationId(), "Hello! This is full information about you and your loan application."
                + " Please, check it and, if it`s right, send signing request by the link: " +
                "http://localhost:8081/deal/document/{applicationId}/sign"
        );
        feignDealClient.sendEmail(emailMessageWithAttachment);
    }


//added by myself for creating documents for the client
    public void createAllDocuments(Application application) throws ResourceNotFoundException {
      // .........
    }


    //POST: /deal/document/{applicationId}/sign - запрос на подписание документов
    @PostMapping("/document/{applicationId}/sign")
    public void requestSign(@PathVariable (value = "applicationId")Long applicationId) throws ResourceNotFoundException {
        NewTopic SendSESTopic = topicsConfig.doSendSESTopic();
        kafkaProducer.sendMessageToTopic(SendSESTopic, "Send ses-code");

        Long sesCode = createSesCode();
        Application application = getApplicationById(applicationId);
        application.setSesCode(sesCode);

        EmailMessage emailMessageWithAttachment = new EmailMessage(
                application.getClient().getEmail(), Theme.SEND_SES,
                application.getApplicationId(), "Hello! There is your personal SES-code: " + sesCode +
                " Please, do not show it anybody and send by the link: " +
                "http://localhost:8081/deal/document/{applicationId}/code"
        );
        feignDealClient.sendEmail(emailMessageWithAttachment);
        application.setApplicationStatus(ApplicationStatus.DOCUMENT_SIGNED);
        application.addToStatusHistoryList(new StatusHistoryJsonb(ApplicationStatus.DOCUMENT_SIGNED,
                LocalDate.now(), ChangeType.AUTOMATIC));
    }

    public long createSesCode(){
        return Long.valueOf((int)(1 + Math.random() * 999));
    }



    //POST: /deal/document/{applicationId}/code - подписание документов
    @PostMapping("/document/{applicationId}/code")
    public void requestSesCode(@PathVariable (value = "applicationId")Long applicationId, Long sesCode) throws ResourceNotFoundException {
        Application application = getApplicationById(applicationId);

        if(application.getSesCode() == sesCode){
            NewTopic creditIssuedTopic = topicsConfig.doCreditIssuedTopic();
            kafkaProducer.sendMessageToTopic(creditIssuedTopic, "Credit issued");

            EmailMessage emailMessageWithAttachment = new EmailMessage(
                    application.getClient().getEmail(), Theme.CREDIT_ISSUED,
                    application.getApplicationId(), "Hello! Your credit issued! Congratulations!"
            );
            feignDealClient.sendEmail(emailMessageWithAttachment);

            application.setApplicationStatus(ApplicationStatus.CREDIT_ISSUED);
            application.addToStatusHistoryList(new StatusHistoryJsonb(ApplicationStatus.CREDIT_ISSUED,
                    LocalDate.now(), ChangeType.AUTOMATIC));
        } else {
            NewTopic applicationDeniedTopic = topicsConfig.doApplicationDeniedTopic();
            kafkaProducer.sendMessageToTopic(applicationDeniedTopic, "Application was denied");

            EmailMessage emailMessage = new EmailMessage(
                    application.getClient().getEmail(), Theme.APPLICATION_DENIED,
                    application.getApplicationId(), "Hello! Your Application was denied! Try " +
                    "to input the correct ses-code by the link again: http://localhost:8081/deal/document/{applicationId}/code "
            );
            feignDealClient.sendEmail(emailMessage);

            application.setApplicationStatus(ApplicationStatus.CLIENT_DENIED);
            application.addToStatusHistoryList(new StatusHistoryJsonb(ApplicationStatus.CLIENT_DENIED,
                    LocalDate.now(), ChangeType.AUTOMATIC));

        }
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

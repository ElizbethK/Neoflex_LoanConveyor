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
import ru.neostudy.loanConveyorProject.deal.enums.CreditStatus;
import ru.neostudy.loanConveyorProject.deal.exception.ResourceNotFoundException;
import ru.neostudy.loanConveyorProject.deal.service.ApplicationService;
import ru.neostudy.loanConveyorProject.deal.service.AttachmentsService;
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
    @Autowired
    AttachmentsService attachmentsService;


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


        String message = "Hello! Your loan application №"
                + loanOfferDTO.getApplicationId()
                + " successfully approved! Please, finish your registration by the link: " +
                "http://localhost:8081/deal/calculate/{applicationId}";
        feignDealClient.sendEmail(createSimpleEmailMessage(updatedApplication, Theme.FINISH_REGISTRATION, message));
        logger.info("Email message sent");
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


        String message = "Hello! Your loan application №"
                + application.getApplicationId()
                + " passed all checks! Please, send creating documents request by the link: " +
                "http://localhost:8081/deal/document/{applicationId}/send";
        feignDealClient.sendEmail(createSimpleEmailMessage(application, Theme.CREATE_DOCUMENTS, message));
        logger.info("Email message sent");

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


        String message = "Hello! This is full information about you and your loan application."
                + " Please, check it and, if it`s right, send signing request by the link: " +
                "http://localhost:8081/deal/document/{applicationId}/sign";
        String attachmentAddress1 = "D:\\NeostudyProject\\LoanConveyor\\src\\main\\resources\\credit-application.txt";
        String attachmentAddress2 = "D:\\NeostudyProject\\LoanConveyor\\src\\main\\resources\\credit-loan.txt";
        String attachmentAddress3 = "D:\\NeostudyProject\\LoanConveyor\\src\\main\\resources\\credit-schedule.txt";
        feignDealClient.sendEmail(createAttachmentEmailMessage(application, Theme.SEND_DOCUMENTS, message,
                attachmentAddress1, attachmentAddress2, attachmentAddress3));
        logger.info("Email message with attachments sent");

    }


    //POST: /deal/document/{applicationId}/sign - запрос на подписание документов
    @PostMapping("/document/{applicationId}/sign")
    public void requestSign(@PathVariable (value = "applicationId")Long applicationId) throws ResourceNotFoundException {
        NewTopic SendSESTopic = topicsConfig.doSendSESTopic();
        kafkaProducer.sendMessageToTopic(SendSESTopic, "Send ses-code");

        Long sesCode = createSesCode();
        Application application = getApplicationById(applicationId);
        application.setSesCode(sesCode);


        String message = "Hello! There is your personal SES-code: " + sesCode +
                " Please, do not show it anybody and send by the link: " +
                "http://localhost:8081/deal/document/{applicationId}/code";
        String attachmentAddress1 = "D:\\NeostudyProject\\LoanConveyor\\src\\main\\resources\\credit-application.txt";
        String attachmentAddress2 = "D:\\NeostudyProject\\LoanConveyor\\src\\main\\resources\\credit-loan.txt";
        String attachmentAddress3 = "D:\\NeostudyProject\\LoanConveyor\\src\\main\\resources\\credit-schedule.txt";
        feignDealClient.sendEmail(createAttachmentEmailMessage(application, Theme.SEND_SES, message,
                attachmentAddress1, attachmentAddress2, attachmentAddress3));
        logger.info("Email message with attachments sent");

        application.setApplicationStatus(ApplicationStatus.DOCUMENT_SIGNED);
        application.addToStatusHistoryList(new StatusHistoryJsonb(ApplicationStatus.DOCUMENT_SIGNED,
                LocalDate.now(), ChangeType.AUTOMATIC));
    }

    //POST: /deal/document/{applicationId}/code - подписание документов
    @PostMapping("/document/{applicationId}/code")
    public void requestSesCode(@PathVariable (value = "applicationId")Long applicationId, Long sesCode) throws ResourceNotFoundException {
        Application application = getApplicationById(applicationId);

        if(application.getSesCode() == sesCode){
            NewTopic creditIssuedTopic = topicsConfig.doCreditIssuedTopic();
            kafkaProducer.sendMessageToTopic(creditIssuedTopic, "Credit issued");


            String message = "Hello! Your credit issued! Congratulations!";
            feignDealClient.sendEmail(createSimpleEmailMessage(application, Theme.CREDIT_ISSUED, message));
            logger.info("Email message sent");

            application.setApplicationStatus(ApplicationStatus.CREDIT_ISSUED);
            application.addToStatusHistoryList(new StatusHistoryJsonb(ApplicationStatus.CREDIT_ISSUED,
                    LocalDate.now(), ChangeType.AUTOMATIC));
            application.getCredit().setCreditStatus(CreditStatus.ISSUED);


        } else {
            NewTopic applicationDeniedTopic = topicsConfig.doApplicationDeniedTopic();
            kafkaProducer.sendMessageToTopic(applicationDeniedTopic, "Application was denied");


            String message = "Hello! Your Application was denied! Try " +
                    "to input the correct ses-code by the link again: http://localhost:8081/deal/document/{applicationId}/code";
            feignDealClient.sendEmail(createSimpleEmailMessage(application, Theme.APPLICATION_DENIED, message));
            logger.info("Email message sent");

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
        logger.info("DealController. Найден {}", applicationList);
        return applicationList;
    }


    //-----------------------------------------------------------------------------------------------------------//
                                        // ---- additional methods -----//

    //for creating documents for the client
    public void createAllDocuments(Application application) throws ResourceNotFoundException {
        logger.info("Формирование всех документов из {}", application);
        attachmentsService.buildCreditApplicationFile(application);
        attachmentsService.buildCreditLoanFile(application);
        attachmentsService.buildCreditPaymentScheduleFile(application);
        logger.info("Документы сформированы");
    }

    //for creating emails for the client
    public EmailMessage createSimpleEmailMessage(Application application, Theme theme, String message){
        return new EmailMessage(
                application.getClient().getEmail(), theme,
                application.getApplicationId(), message
        );
    }

    //for creating email with attachments for the client
    public EmailMessage createAttachmentEmailMessage(Application application, Theme theme, String message, String ...attachments){
        return new EmailMessage(
                application.getClient().getEmail(), theme,
                application.getApplicationId(), message, attachments
        );
    }

    //for creating ses-code
    public long createSesCode(){
        logger.info("Создание ses - кода");
        Long sesCode = Long.valueOf((int)(1 + Math.random() * 999));
        logger.info("Создан ses-код = {}", sesCode);
        return sesCode;
    }


}

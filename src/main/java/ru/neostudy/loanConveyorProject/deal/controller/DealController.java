package ru.neostudy.loanConveyorProject.deal.controller;


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
        applicationService.updateApplication(loanOfferDTO);
        logger.info("DealController. Запущен метод updateApplication");
    }





   /*
    PUT: /deal/calculate/{applicationId} - завершение регистрации + полный подсчёт кредита.
    Request - FinishRegistrationRequestDTO, param - Long,  response void.
    */
    @PutMapping("/calculate/{applicationId}")
    public void finishRegistration(@PathVariable(value = "applicationId") Integer id,
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
    }





}

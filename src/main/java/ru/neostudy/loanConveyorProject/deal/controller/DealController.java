package ru.neostudy.loanConveyorProject.deal.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    ClientService clientService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    ScoringDataDTOService scoringDataDTOService;

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

        Client client = clientService.createClient(loanApplicationRequestDTO);
        Application application = applicationService.createApplication(client);

        ConveyorController conveyorController = new ConveyorController();
        return conveyorController.getPossibleOffers( loanApplicationRequestDTO, bindingResult);
    }




   /*
    PUT: /deal/offer - Выбор одного из предложений.
    Request LoanOfferDTO, response void.*/

    @PutMapping("/offer")
    public void chooseOffer(@RequestBody LoanOfferDTO loanOfferDTO) throws ResourceNotFoundException {
        applicationService.chooseLoanOffer(loanOfferDTO);
    }





   /*
    PUT: /deal/calculate/{applicationId} - завершение регистрации + полный подсчёт кредита.
    Request - FinishRegistrationRequestDTO, param - Long,  response void.
    */
    @PutMapping("/calculate/{applicationId}")
    public void finishRegistration(@PathVariable(value = "applicationId") Long id,
                                   @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                   BindingResult bindingResult) throws ResourceNotFoundException {
        Optional <Application> optionalApplication = applicationService.findById(id);
        optionalApplication.orElseThrow(() ->
                new ResourceNotFoundException("Application with id " + id + " is not found"));

        Application application = optionalApplication.get();
        ScoringDataDTO scoringDataDTO = scoringDataDTOService.consolidateScoringInformation(application,finishRegistrationRequestDTO);

        ConveyorController conveyorController = new ConveyorController();
        conveyorController.getCalculatedCredit(scoringDataDTO, bindingResult);
    }





}

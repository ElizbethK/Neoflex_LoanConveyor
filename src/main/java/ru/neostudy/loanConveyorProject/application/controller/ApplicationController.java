package ru.neostudy.loanConveyorProject.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.deal.exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    private FeignApplicationClient feignApplicationClient;

//    POST: /application - Прескоринг + запрос на расчёт возможных условий кредита.
//    Request - LoanApplicationRequestDTO, response List<LoanOfferDTO>

    @PostMapping
    public List<LoanOfferDTO> createFourOffers(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO,
                                               BindingResult bindingResult){
        return feignApplicationClient.getPossibleOffers(loanApplicationRequestDTO);

    }


    //PUT: /application/offer - Выбор одного из предложений.
    //Request LoanOfferDTO, response void.
    @PutMapping("/offer")
    public void takeOffer(@RequestBody LoanOfferDTO loanOfferDTO) throws ResourceNotFoundException{
        feignApplicationClient.chooseOffer(loanOfferDTO);
    }



}

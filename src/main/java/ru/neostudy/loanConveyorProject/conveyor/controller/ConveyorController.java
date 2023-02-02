package ru.neostudy.loanConveyorProject.conveyor.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.neostudy.loanConveyorProject.conveyor.dto.*;
import ru.neostudy.loanConveyorProject.conveyor.service.LoanOfferService;
import ru.neostudy.loanConveyorProject.conveyor.service.ScoringService;
import ru.neostudy.loanConveyorProject.conveyor.util.LoanApplicationErrorResponse;
import ru.neostudy.loanConveyorProject.conveyor.util.LoanApplicationNotCreatedException;
import ru.neostudy.loanConveyorProject.deal.service.ScoringDataDTOService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/conveyor")
public class ConveyorController {
    private static final Logger logger = LoggerFactory.getLogger(ConveyorController.class);


    @Autowired
    LoanOfferService loanOfferService;

    @Autowired
    ScoringService scoringService;




//  POST: /conveyor/offers - расчёт возможных условий кредита.
//  Request - LoanApplicationRequestDTO, response - List<LoanOfferDTO>


    @PostMapping("/offers")
    public List<LoanOfferDTO> getPossibleOffers(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO,
                                                BindingResult bindingResult){
        logger.info("ConveyorController. Запущен метод getPossibleOffers с {}", loanApplicationRequestDTO);
        if (bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors ){
               errorMsg.append(error.getField())
                       .append(" - ").append(error.getDefaultMessage())
                       .append(";");
            }
            throw new LoanApplicationNotCreatedException(errorMsg.toString());
        }

        loanOfferService = new LoanOfferService();
        List<LoanOfferDTO> loanOfferDTOList = loanOfferService.createLoanOffers(loanApplicationRequestDTO);
        logger.info("ConveyorController. Завершен getPossibleOffers. Создан {}", loanOfferDTOList);
        return loanOfferDTOList;
    }



//    POST: /conveyor/calculation - валидация присланных данных + скоринг данных + полный расчет параметров кредита.
//    Request - ScoringDataDTO, response CreditDTO.

   @PostMapping("/calculation")
   public CreditDTO getCalculatedCredit(@RequestBody @Valid ScoringDataDTO scoringDataDTO,
                                        BindingResult bindingResult){
       logger.info("ConveyorController. Запущен метод getCalculatedCredit c {}", scoringDataDTO);
       if (bindingResult.hasErrors()){
           StringBuilder errorMsg = new StringBuilder();

           List<FieldError> errors = bindingResult.getFieldErrors();
           for(FieldError error : errors ){
               errorMsg.append(error.getField())
                       .append(" - ").append(error.getDefaultMessage())
                       .append(";");
           }
           throw new LoanApplicationNotCreatedException(errorMsg.toString());
       }
       scoringService = new ScoringService();
       CreditDTO creditDTO = scoringService.score(scoringDataDTO);
       logger.info("ConveyorController. Завершен getCalculatedCredit. {}", creditDTO);
       return creditDTO;
   }



    @ExceptionHandler
    private ResponseEntity<LoanApplicationErrorResponse> handleException(
            LoanApplicationNotCreatedException e){
        LoanApplicationErrorResponse response = new LoanApplicationErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

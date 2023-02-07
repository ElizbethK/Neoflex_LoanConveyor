package ru.neostudy.loanConveyorProject.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.neostudy.loanConveyorProject.conveyor.controller.ConveyorController;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.deal.exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);


    @Autowired
    private FeignApplicationClient feignApplicationClient;

//    POST: /application - Прескоринг + запрос на расчёт возможных условий кредита.
//    Request - LoanApplicationRequestDTO, response List<LoanOfferDTO>

    @PostMapping
    public List<LoanOfferDTO> createFourOffers(@RequestBody @Valid LoanApplicationRequestDTO loanApplicationRequestDTO,
                                               BindingResult bindingResult){
        logger.info("ApplicationController. Запущен метод createFourOffers с {}", loanApplicationRequestDTO);

        logger.info("DealController. Переход на ConveyorController. (метод getPossibleOffers)");
        return feignApplicationClient.getPossibleOffers(loanApplicationRequestDTO);

    }


    //PUT: /application/offer - Выбор одного из предложений.
    //Request LoanOfferDTO, response void.
    @PutMapping("/offer")
    public void takeOffer(@RequestBody LoanOfferDTO loanOfferDTO) throws ResourceNotFoundException{
        logger.info("ApplicationController. Запущен метод takeOffer с {}", loanOfferDTO);

        logger.info("ApplicationController. Переход на DealController. (метод chooseOffer)");
        feignApplicationClient.chooseOffer(loanOfferDTO);
    }



}

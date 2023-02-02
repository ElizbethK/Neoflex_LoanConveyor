package ru.neostudy.loanConveyorProject.deal.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neostudy.loanConveyorProject.conveyor.dto.CreditDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "dealClient", url = "http://localhost:8080/conveyor")
public interface FeignDealClient {


        @PostMapping("/offers")
         List<LoanOfferDTO> getPossibleOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

        @PostMapping("/calculation")
         CreditDTO getCalculatedCredit(@RequestBody ScoringDataDTO scoringDataDTO);


}

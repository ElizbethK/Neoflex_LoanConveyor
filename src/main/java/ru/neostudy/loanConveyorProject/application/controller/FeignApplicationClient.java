package ru.neostudy.loanConveyorProject.application.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.deal.exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "applicationClient", url = "http://localhost:8081/deal")
public interface FeignApplicationClient {

    @PostMapping("/application")
    public List<LoanOfferDTO> getPossibleOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PutMapping("/offer")
    public void chooseOffer(@RequestBody LoanOfferDTO loanOfferDTO) throws ResourceNotFoundException;

}

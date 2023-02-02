package ru.neostudy.loanConveyorProject.conveyor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LoanOfferServiceTest {

    @Mock
    LoanOfferDTO loanOfferDTO = new LoanOfferDTO();


    @InjectMocks
    LoanOfferService loanOfferService = new LoanOfferService();


    @Test
   void createLoanOffers() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();
        BigDecimal amount = new BigDecimal(100063);
        int term = 24;
        loanApplicationRequestDTO.setAmount(amount);
        loanApplicationRequestDTO.setTerm(term);

        List<LoanOfferDTO> loanOfferDTOS = loanOfferService.createLoanOffers(loanApplicationRequestDTO);


        assertEquals(4, loanOfferDTOS.size());
        assertEquals(amount, loanOfferDTOS.get(0).getRequestedAmount());
        assertEquals(term, loanOfferDTOS.get(0).getTerm());

    }


    @Test
    void calculateMonthPayment() {
        when(loanOfferDTO.getRate()).thenReturn(BigDecimal.valueOf(10));
        when(loanOfferDTO.getRequestedAmount()).thenReturn(BigDecimal.valueOf(10050));
        when(loanOfferDTO.getTerm()).thenReturn(24);
        assertEquals(BigDecimal.valueOf(4633050, 4), loanOfferService.calculateMonthPayment(loanOfferDTO));

    }
}
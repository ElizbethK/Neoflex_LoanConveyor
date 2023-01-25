package ru.neostudy.loanConveyorProject.conveyor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanOfferServiceTest {

    @Mock
    private LoanApplicationRequestDTO loanApplicationRequestDTO;

    LoanOfferDTO loanOfferDTO;

    @InjectMocks
    private LoanOfferService loanOfferService;

    public LoanOfferServiceTest(){
        this.loanOfferService = new LoanOfferService(loanApplicationRequestDTO);
    }


    @Test
    void createLoanOffers() {
    }

    @Test
    void createOfferDTO() {
    }

    @Test
    void calculateMonthPaymentShouldReturn() {
//        loanOfferDTO = new LoanOfferDTO();
//        loanOfferDTO.setRate(BigDecimal.valueOf(10));
//        loanOfferDTO.setRequestedAmount(BigDecimal.valueOf(1000400));
        given(loanOfferDTO.getRate()).willReturn(BigDecimal.valueOf(10));
        given(loanOfferDTO.getRequestedAmount()).willReturn(BigDecimal.valueOf(100500));

        assertEquals(BigDecimal.valueOf(10), loanOfferService.calculateMonthPayment(loanOfferDTO));


    }
}
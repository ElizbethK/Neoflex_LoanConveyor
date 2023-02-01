package ru.neostudy.loanConveyorProject.conveyor.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neostudy.loanConveyorProject.deal.entity.Application;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LoanOfferServiceTest {

    @Mock
    private Application application;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createLoanOffers() {
    }

    @Test
    void createOfferDTO() {
    }

    @Test
    void calculateMonthPayment() {
       /* given(loanOfferDTO.getRate()).willReturn(BigDecimal.valueOf(10));
        given(loanOfferDTO.getRequestedAmount()).willReturn(BigDecimal.valueOf(100500));

        assertEquals(BigDecimal.valueOf(10), loanOfferService.calculateMonthPayment(loanOfferDTO));
*/
    }
}
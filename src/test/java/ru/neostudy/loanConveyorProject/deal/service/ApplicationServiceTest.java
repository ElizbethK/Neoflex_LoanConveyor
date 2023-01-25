package ru.neostudy.loanConveyorProject.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Application;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.exception.ResourceNotFoundException;
import ru.neostudy.loanConveyorProject.deal.repository.ApplicationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    @Mock
    Client client;

    @Mock
    ApplicationRepository applicationRepository;

    @InjectMocks
    ApplicationService applicationService = new ApplicationService(applicationRepository);


    @Test
    void chooseLoanOfferTest() throws ResourceNotFoundException {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setApplicationId(1l);
        //given(loanOfferDTO.getApplicationId()).willReturn(1L);
        when(applicationRepository.findById(1l))
                .thenReturn(java.util.Optional.of(new Application()));

        applicationService.chooseLoanOffer(new LoanOfferDTO());
        verify(applicationService, times(1)).chooseLoanOffer(loanOfferDTO);
    }

    @Test
    void findByIdTest() {
        when(applicationRepository.findById(1l)).thenReturn(Optional.of(new Application()));
        List<Optional> optionalList = new ArrayList<>();
        optionalList.add(applicationService.findById(1l));
        optionalList.add(applicationService.findById(2l));
        optionalList.add(applicationService.findById(3l));

        assertEquals(3, optionalList.size());
    }
}
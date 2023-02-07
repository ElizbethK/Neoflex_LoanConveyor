package ru.neostudy.loanConveyorProject.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Application;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.entity.StatusHistoryJsonb;
import ru.neostudy.loanConveyorProject.deal.enums.ApplicationStatus;
import ru.neostudy.loanConveyorProject.deal.enums.ChangeType;
import ru.neostudy.loanConveyorProject.deal.exception.ResourceNotFoundException;
import ru.neostudy.loanConveyorProject.deal.repository.ApplicationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.neostudy.loanConveyorProject.deal.enums.ApplicationStatus.PREAPPROVAL;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    @Mock
    ApplicationRepository applicationRepository;


    @InjectMocks
    ApplicationService applicationService;

    @Test
    void createApplication() {
        Client client = new Client();
        client.setClientId(112L);
        ApplicationStatus applicationStatus = PREAPPROVAL;
        StatusHistoryJsonb statusHistoryJsonb = new StatusHistoryJsonb(
                ApplicationStatus.PREAPPROVAL, LocalDate.now(), ChangeType.AUTOMATIC );

        Application application =  applicationService.createApplication(client);

        assertEquals(applicationStatus, application.getApplicationStatus());
        assertEquals(client, application.getClient());
        assertEquals(statusHistoryJsonb, application.getStatusHistoryList().get(0));


        assertNotNull(applicationService.createApplication(client).getClient());
        verify(applicationRepository,times(2)).save(application);

    }

    @Test
    void updateApplication() throws ResourceNotFoundException {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        Long id = Long.valueOf(44);
        loanOfferDTO.setApplicationId(id);
        Optional <Application> optionalApplication = Optional.of(new Application());
        when(applicationRepository.findById(id)).thenReturn(optionalApplication);
        Application application = optionalApplication.get();

        applicationService.updateApplication(loanOfferDTO);

        verify(applicationRepository, times(1)).save(application);
        verify(applicationRepository, times(1)).findById(id);

    }


    @Test
    void findById() throws ResourceNotFoundException {
        Long id = Long.valueOf(12);
        Application application = new Application();
        application.setApplicationId(id);
        applicationRepository.save(application);
        Optional<Application> applicationOptional = applicationRepository.findById(id);

        assertEquals(applicationOptional, applicationRepository.findById(id));
        assertNotNull(applicationRepository.findById(id));
        verify(applicationRepository, times(3)).findById(id);
    }

    @Test
    void setIDForEachOffer() {
        List<LoanOfferDTO> loanOfferDTOList =
                List.of(
                        new LoanOfferDTO(),
                        new LoanOfferDTO(),
                        new LoanOfferDTO(),
                        new LoanOfferDTO()
                );
        Long id = Long.valueOf(14263);
        Application application = new Application();
        application.setApplicationId(id);

        applicationService.setIDForEachOffer(loanOfferDTOList, application);

        LoanOfferDTO loanOfferDTO1 = loanOfferDTOList.get(0);
        LoanOfferDTO loanOfferDTO2 = loanOfferDTOList.get(1);
        LoanOfferDTO loanOfferDTO3 = loanOfferDTOList.get(2);
        LoanOfferDTO loanOfferDTO4 = loanOfferDTOList.get(3);


        assertEquals(id, loanOfferDTO1.getApplicationId());
        assertEquals(id, loanOfferDTO2.getApplicationId());
        assertEquals(id, loanOfferDTO3.getApplicationId());
        assertEquals(id, loanOfferDTO4.getApplicationId());


    }
}
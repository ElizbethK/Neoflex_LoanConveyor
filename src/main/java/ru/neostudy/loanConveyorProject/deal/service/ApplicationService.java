package ru.neostudy.loanConveyorProject.deal.service;

import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Application;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.entity.StatusHistoryJsonb;
import ru.neostudy.loanConveyorProject.deal.enums.ApplicationStatus;
import ru.neostudy.loanConveyorProject.deal.enums.ChangeType;
import ru.neostudy.loanConveyorProject.deal.exception.ResourceNotFoundException;
import ru.neostudy.loanConveyorProject.deal.repository.ApplicationRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ApplicationService {
    ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application createApplication(Client client){
        Application application = new Application();
        application.setClient(client);
        application.setCreationDate(LocalDate.now());

        application.setApplicationStatus(ApplicationStatus.PREAPPROVAL);
        StatusHistoryJsonb statusHistoryJsonb = new StatusHistoryJsonb();
        statusHistoryJsonb.setStatus(ApplicationStatus.PREAPPROVAL);
        statusHistoryJsonb.setTime(LocalDate.now());
        statusHistoryJsonb.setChangeType(ChangeType.AUTOMATIC);
        application.setStatusHistoryList(statusHistoryJsonb);

        applicationRepository.save(application);

        return application;
    }


    public void chooseLoanOffer(LoanOfferDTO loanOfferDTO) throws ResourceNotFoundException {
        Long id = loanOfferDTO.getApplicationId();

        Optional <Application> optionalApplication = applicationRepository.findById(id);
        Application application = optionalApplication.get();
        application.setApplicationStatus(ApplicationStatus.APPROVED);

        StatusHistoryJsonb statusHistoryJsonb = new StatusHistoryJsonb();
        statusHistoryJsonb.setStatus(ApplicationStatus.APPROVED);
        statusHistoryJsonb.setTime(LocalDate.now());
        statusHistoryJsonb.setChangeType(ChangeType.AUTOMATIC);
        application.setStatusHistoryList(statusHistoryJsonb);

        application.setAppliedOffer(loanOfferDTO);

        applicationRepository.save(application);

    }

    public Optional<Application> findById(Long id){
        return applicationRepository.findById(id);
    }




}

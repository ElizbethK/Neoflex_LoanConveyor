package ru.neostudy.loanConveyorProject.deal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application createApplication(Client client){
        logger.info("Создание Application из client = {}", client);
        Application application = new Application();
        application.setClient(client);
        application.setCreationDate(LocalDate.now());

        application.setApplicationStatus(ApplicationStatus.PREAPPROVAL);

        StatusHistoryJsonb statusHistoryJsonb = new StatusHistoryJsonb();
        statusHistoryJsonb.setStatus(ApplicationStatus.PREAPPROVAL);
        statusHistoryJsonb.setTime(LocalDate.now());
        statusHistoryJsonb.setChangeType(ChangeType.AUTOMATIC);
        application.addToStatusHistoryList(statusHistoryJsonb);

        applicationRepository.save(application);
        logger.info("Сохранен новый Application = {}", application);

        return application;
    }


    public Application updateApplication(LoanOfferDTO loanOfferDTO) throws ResourceNotFoundException {
        logger.info("Обновление Application из: {}", loanOfferDTO);
        Long id = loanOfferDTO.getApplicationId();

        Optional <Application> optionalApplication = applicationRepository.findById(id);
        Application application = optionalApplication.get();
        logger.info("Application до обновления: = {}", application);
        application.setApplicationStatus(ApplicationStatus.APPROVED);

        StatusHistoryJsonb statusHistoryJsonb = new StatusHistoryJsonb();
        statusHistoryJsonb.setStatus(ApplicationStatus.APPROVED);
        statusHistoryJsonb.setTime(LocalDate.now());
        statusHistoryJsonb.setChangeType(ChangeType.AUTOMATIC);
        application.addToStatusHistoryList(statusHistoryJsonb);

        application.setAppliedOffer(loanOfferDTO);
        logger.info("Application обновлен: = {}", application);
        applicationRepository.save(application);
        logger.info("Обновленный Application сохранен");

        return application;

    }






    public Optional<Application> findById(Long id) throws ResourceNotFoundException{
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        Application application = optionalApplication.get();
        logger.info("Найден Application с id = {}: {}", id, application );
        return optionalApplication;
    }


    public List<LoanOfferDTO> setIDForEachOffer(List<LoanOfferDTO> loanOfferDTOList, Application application){
        for (LoanOfferDTO loanOfferDTO:loanOfferDTOList
             ) {
            loanOfferDTO.setApplicationId(application.getApplicationId());
        }
        logger.info("Для каждого LoanOfferDTO установлен ApplicationId = {}", application.getApplicationId());
        return loanOfferDTOList;
    }

    public List<Application> getAllApplications(){
        List<Application> applicationList = new ArrayList<>();
        Iterable<Application> applications = applicationRepository.findAll();
        for (Application application : applications
             ) {applicationList.add(application);
        }
        return applicationList;
    }

}

package ru.neostudy.loanConveyorProject.deal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.conveyor.service.ScoringService;
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
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    @Autowired
    ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        logger.info("Конструктор ApplicationService");
        this.applicationRepository = applicationRepository;

    }

    public Application createApplication(Client client){
        logger.info("Начало метода createApplication(Client client)");
        Application application = new Application();
        logger.info("Сохранение поля сlient в application");
        application.setClient(client);
        application.setCreationDate(LocalDate.now());

        logger.info("Установка статуса");
        application.setApplicationStatus(ApplicationStatus.PREAPPROVAL);
        logger.info("Создание statusHistoryJsonb");
        StatusHistoryJsonb statusHistoryJsonb = new StatusHistoryJsonb();
        logger.info("Установка нового статуса");
        statusHistoryJsonb.setStatus(ApplicationStatus.PREAPPROVAL);
        logger.info("Установка даты изменения статуса");
        statusHistoryJsonb.setTime(LocalDate.now());
        logger.info("Установка типа смены статуса");
        statusHistoryJsonb.setChangeType(ChangeType.AUTOMATIC);

        logger.info("Добавление statusHistoryJsonb в List ");
        application.setStatusHistoryList(statusHistoryJsonb);

        logger.info("Сохранение обновленного application в БД");
        applicationRepository.save(application);

        return application;
    }


    public void chooseLoanOffer(LoanOfferDTO loanOfferDTO) throws ResourceNotFoundException {
        logger.info("Начало метода chooseLoanOffer(LoanOfferDTO loanOfferDTO)");
        Long id = loanOfferDTO.getApplicationId();

        logger.info("Поиск application по id");
        Optional <Application> optionalApplication = applicationRepository.findById(id);
        logger.info("Найден");
        Application application = optionalApplication.get();
        logger.info("Обновление статуса");
        application.setApplicationStatus(ApplicationStatus.APPROVED);

        logger.info("Создание statusHistoryJsonb");
        StatusHistoryJsonb statusHistoryJsonb = new StatusHistoryJsonb();
        logger.info("Установка нового статуса");
        statusHistoryJsonb.setStatus(ApplicationStatus.APPROVED);
        logger.info("Установка даты изменения статуса");
        statusHistoryJsonb.setTime(LocalDate.now());
        logger.info("Установка типа смены статуса");
        statusHistoryJsonb.setChangeType(ChangeType.AUTOMATIC);
        logger.info("Добавление statusHistoryJsonb в List ");
        application.setStatusHistoryList(statusHistoryJsonb);

        logger.info("Добавление loanOfferDTO");
        application.setAppliedOffer(loanOfferDTO);

        logger.info("Сохранение обновленного application в БД");
        applicationRepository.save(application);

    }


    public Optional<Application> findById(Long id){
        logger.info("Выполнен метод Optional<Application> findById(Long id)");
        return applicationRepository.findById(id);

    }




}

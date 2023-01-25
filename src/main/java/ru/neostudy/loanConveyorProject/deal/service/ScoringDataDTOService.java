package ru.neostudy.loanConveyorProject.deal.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.FinishRegistrationRequestDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Application;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.entity.Passport;

@Service
public class ScoringDataDTOService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);


    @Autowired
    private ScoringDataDTO scoringDataDTO;


    public ScoringDataDTOService(ScoringDataDTO scoringDataDTO) {
        logger.info("Конструктор ScoringDataDTOService");
        this.scoringDataDTO = scoringDataDTO;
    }


    public ScoringDataDTO consolidateScoringInformation(Application application,
                                              FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        logger.info("Запуск метода consolidateScoringInformation");
        logger.info("Создание экземпляра Client из Application");
        Client client = application.getClient();

        logger.info("Присваивание значений полям scoringDataDTO из поступивших в метод application и finishRegistrationRequestDTO");
        logger.info("Установлена сумма");
        scoringDataDTO.setAmount(finishRegistrationRequestDTO.getDependentAmount());
        logger.info("Установлено ФИО");
        scoringDataDTO.setFirstName(client.getFirstName());
        scoringDataDTO.setLastName(client.getLastName());
        scoringDataDTO.setMiddleName(client.getMiddleName());
        logger.info("Установлен гендер");
        scoringDataDTO.setGender(finishRegistrationRequestDTO.getGender());
        logger.info("Установлена дата рождения");
        scoringDataDTO.setBirthdate(client.getBirthDate());
        logger.info("Установлено семейное положение");
        scoringDataDTO.setMaritalStatus(client.getMaritalStatus());
        logger.info("Установлено кол-во иждивенцев");
        scoringDataDTO.setDependentAmount(client.getDependentAmount());
        logger.info("Установлен Employment");
        scoringDataDTO.setEmployment(finishRegistrationRequestDTO.getEmployment());
        logger.info("Установлен счет");
        scoringDataDTO.setAccount(finishRegistrationRequestDTO.getAccount());

        logger.info("Выделение экземпляра passport из поля класса Client");
        Passport passport = client.getPassport();

        logger.info("Заполнение полей passport из поступивших в метод client и finishRegistrationRequestDTO");
        scoringDataDTO.setPassportNumber(passport.getNumber());
        scoringDataDTO.setPassportSeries(passport.getSeries());
        scoringDataDTO.setPassportIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        scoringDataDTO.setPassportIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());

//        scoringDataDTO.setIsInsuranceEnabled();
//        scoringDataDTO.setIsSalaryClient();


        logger.info("Возвращение заполненного scoringDataDTO");

        return scoringDataDTO;
    }
}

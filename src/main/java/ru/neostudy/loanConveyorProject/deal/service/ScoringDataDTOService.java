package ru.neostudy.loanConveyorProject.deal.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.FinishRegistrationRequestDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Application;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.entity.Credit;
import ru.neostudy.loanConveyorProject.deal.entity.Passport;

@Service
public class ScoringDataDTOService {
    private static final Logger logger = LoggerFactory.getLogger(ScoringDataDTOService.class);

    public ScoringDataDTO consolidateScoringInformation(Application application,
                                              FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        logger.info("Вызван метод consolidateScoringInformation с параметрами: {}, {}",
                application, finishRegistrationRequestDTO);

        Client client = application.getClient();
        Credit credit = application.getCredit();

        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        logger.info("Создан {}", scoringDataDTO);

        scoringDataDTO.setAmount(finishRegistrationRequestDTO.getDependentAmount());
        scoringDataDTO.setTerm(credit.getTerm());
        scoringDataDTO.setFirstName(client.getFirstName());
        scoringDataDTO.setLastName(client.getLastName());
        scoringDataDTO.setMiddleName(client.getMiddleName());
        scoringDataDTO.setGender(finishRegistrationRequestDTO.getGender());
        scoringDataDTO.setBirthdate(client.getBirthDate());

        Passport passport = client.getPassport();
        scoringDataDTO.setPassportNumber(passport.getNumber());
        scoringDataDTO.setPassportSeries(passport.getSeries());
        scoringDataDTO.setPassportIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        scoringDataDTO.setPassportIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());

        scoringDataDTO.setMaritalStatus(client.getMaritalStatus());
        scoringDataDTO.setDependentAmount(client.getDependentAmount());

        scoringDataDTO.setEmployment(finishRegistrationRequestDTO.getEmployment());
        scoringDataDTO.setAccount(finishRegistrationRequestDTO.getAccount());
        scoringDataDTO.setIsSalaryClient(credit.isSalaryClient());
        scoringDataDTO.setIsInsuranceEnabled(credit.isInsuranceEnable());

        logger.info("Заполнен {}", scoringDataDTO);
        logger.info("Метод consolidateScoringInformation завершен");
        return scoringDataDTO;

    }
}

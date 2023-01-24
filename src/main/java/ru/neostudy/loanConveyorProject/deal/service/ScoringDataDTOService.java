package ru.neostudy.loanConveyorProject.deal.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.FinishRegistrationRequestDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Application;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.entity.Passport;

@Service
public class ScoringDataDTOService {

    @Autowired
    private ScoringDataDTO scoringDataDTO = new ScoringDataDTO();

    public ScoringDataDTOService(ScoringDataDTO scoringDataDTO) {
        this.scoringDataDTO = scoringDataDTO;
    }


    public ScoringDataDTO consolidateScoringInformation(Application application,
                                              FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        Client client = application.getClient();

        scoringDataDTO.setAmount(finishRegistrationRequestDTO.getDependentAmount());
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

//        scoringDataDTO.setIsInsuranceEnabled();
//        scoringDataDTO.setIsSalaryClient();

        return scoringDataDTO;
    }
}

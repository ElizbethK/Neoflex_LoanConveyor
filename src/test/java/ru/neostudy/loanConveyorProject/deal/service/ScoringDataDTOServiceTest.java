package ru.neostudy.loanConveyorProject.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.neostudy.loanConveyorProject.conveyor.dto.FinishRegistrationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Application;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.entity.Credit;
import ru.neostudy.loanConveyorProject.deal.entity.Passport;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoringDataDTOServiceTest {

    @Autowired
    ScoringDataDTOService scoringDataDTOService = new ScoringDataDTOService();

    @Test
    void consolidateScoringInformation() {
        Application application = new Application();
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = new FinishRegistrationRequestDTO();

        String lastName1 = "Smirnov";
        String firstName1 = "Alexey";
        String middleName1 = "Igorevich";
        String email1 = "djhbj@kjfn.ru";
        LocalDate birthdate1 = LocalDate.of(1996, 04, 03);
        int term = 24;


        Client client = new Client();
        client.setLastName(lastName1);
        client.setFirstName(firstName1);
        client.setMiddleName(middleName1);
        client.setBirthDate(birthdate1);
        client.setEmail(email1);
        application.setClient(client);

        Passport passport = new Passport();
        client.setPassport(passport);

        Credit credit = new Credit();
        credit.setTerm(term);
        application.setCredit(credit);


        ScoringDataDTO scoringDataDTO = scoringDataDTOService.consolidateScoringInformation(application, finishRegistrationRequestDTO);

        assertEquals(lastName1, scoringDataDTO.getLastName());
        assertEquals(firstName1, scoringDataDTO.getFirstName());
        assertEquals(middleName1, scoringDataDTO.getMiddleName());
        assertNull(scoringDataDTO.getPassportIssueBranch());
        assertNull(scoringDataDTO.getGender());
        assertNull(scoringDataDTO.getPassportSeries());
        assertNull(scoringDataDTO.getPassportNumber());
        assertNull(scoringDataDTO.getDependentAmount());

    }


    @Test
    void consolidateScoringInformation1(){
        Application application = new Application();
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = new FinishRegistrationRequestDTO();
        Client client = new Client();
        application.setClient(client);


        Passport passport = new Passport();
        client.setPassport(passport);

        Credit credit = new Credit();

        application.setCredit(credit);

        ScoringDataDTO scoringDataDTOEmpty = new ScoringDataDTO();

        assertNotNull(scoringDataDTOService.consolidateScoringInformation(application, finishRegistrationRequestDTO));

        assertNotEquals(scoringDataDTOEmpty, scoringDataDTOService.consolidateScoringInformation(application, finishRegistrationRequestDTO));

    }

}
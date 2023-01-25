package ru.neostudy.loanConveyorProject.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neostudy.loanConveyorProject.conveyor.dto.FinishRegistrationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;
import ru.neostudy.loanConveyorProject.conveyor.enums.Gender;
import ru.neostudy.loanConveyorProject.conveyor.enums.MaritalStatus;
import ru.neostudy.loanConveyorProject.deal.entity.Application;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.entity.Employment;
import ru.neostudy.loanConveyorProject.deal.entity.Passport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScoringDataDTOServiceTest {
    @Mock
    ScoringDataDTO scoringDataDTO;
    @InjectMocks
    ScoringDataDTOService scoringDataDTOService;

    public ScoringDataDTOServiceTest() {
        this.scoringDataDTOService = new ScoringDataDTOService(scoringDataDTO);
    }

    @Test
    void consolidateScoringInformation() {
        Application application = new Application();
        Client client = new Client(1l, "Smirnoff", "Lan", "Baye", LocalDate.parse("1978-11-11"), "dhsgh@shd.ru", Gender.MALE, MaritalStatus.DIVORCED, 2, new Passport(), new Employment(), "12kdb23j", application);
        FinishRegistrationRequestDTO finishRegistrationRequestDTO
                = new FinishRegistrationRequestDTO(Gender.MALE, MaritalStatus.SINGLE, BigDecimal.valueOf(5000000), LocalDate.parse("1990-05-04"), "MVD", null ,"account" );

        List<ScoringDataDTO> scoringDataDTOList = new ArrayList<>();
        scoringDataDTOList.add(scoringDataDTOService.consolidateScoringInformation(application, finishRegistrationRequestDTO));

        assertEquals(1, scoringDataDTOService.consolidateScoringInformation(application, finishRegistrationRequestDTO));

    }
}
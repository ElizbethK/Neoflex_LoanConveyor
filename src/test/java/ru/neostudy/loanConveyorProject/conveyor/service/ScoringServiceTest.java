package ru.neostudy.loanConveyorProject.conveyor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neostudy.loanConveyorProject.conveyor.dto.CreditDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.EmploymentDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;
import ru.neostudy.loanConveyorProject.conveyor.enums.EmploymentStatus;
import ru.neostudy.loanConveyorProject.conveyor.enums.Gender;
import ru.neostudy.loanConveyorProject.conveyor.enums.MaritalStatus;
import ru.neostudy.loanConveyorProject.conveyor.enums.Position;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {


    @Mock
    private ScoringDataDTO scoringDataDTO;
    @Mock
    private EmploymentDTO employmentDTO;
    @Mock
    private CreditDTO creditDTO = new CreditDTO();


    @InjectMocks
    private ScoringService scoringService;


    public ScoringServiceTest() {
        this.scoringService = new ScoringService(scoringDataDTO);
    }

    @Test
    void score() {




    }

    @Test
    void determineStatusJobShouldBeEMPLOYED() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(employmentDTO.getEmploymentStatus()).willReturn(EmploymentStatus.EMPLOYED);
        assertEquals(BigDecimal.valueOf(10), scoringService.determineStatusJob(scoredRate));
    }

    @Test
    void determineStatusJobShouldBeUNEMPLOYED() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(employmentDTO.getEmploymentStatus()).willReturn(EmploymentStatus.UNEMPLOYED);
        assertEquals(BigDecimal.valueOf(10), scoringService.determineStatusJob(scoredRate));
    }

    @Test
    void determineStatusJobShouldBeSELFEMPLOYED() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(employmentDTO.getEmploymentStatus()).willReturn(EmploymentStatus.SELFEMPLOYED);
        assertEquals(BigDecimal.valueOf(11), scoringService.determineStatusJob(scoredRate));
    }

    @Test
    void determineStatusJobShouldBeBUSINESSOWNER() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(employmentDTO.getEmploymentStatus()).willReturn(EmploymentStatus.BUSINESSOWNER);
        assertEquals(BigDecimal.valueOf(13), scoringService.determineStatusJob(scoredRate));
    }


    @Test
    void determinePositionShouldBeMANAGER() throws Exception {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(employmentDTO.getPosition()).willReturn(Position.MID_MANAGER);
        assertEquals(BigDecimal.valueOf(8), scoringService.determinePosition(scoredRate));
    }

    @Test
    void determinePositionShouldBeTOPMANAGER() throws Exception{
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(this.employmentDTO.getPosition()).willReturn(Position.TOPMANAGER);
        assertEquals(BigDecimal.valueOf(6), scoringService.determinePosition(scoredRate));
    }

    @Test
    void determinePositionShouldBeLABORER() throws Exception{
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(this.employmentDTO.getPosition()).willReturn(Position.WORKER);
        assertEquals(BigDecimal.valueOf(10), scoringService.determinePosition(scoredRate));
    }


    @Test
    void determineMarStatusShouldBeMARRIED() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getMaritalStatus()).willReturn(MaritalStatus.MARRIED);
        assertEquals((BigDecimal.valueOf(7)), scoringService.determineMarStatus(scoredRate));
    }

    @Test
    void determineMarStatusShouldBeDIVORCED() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getMaritalStatus()).willReturn(MaritalStatus.DIVORCED);
        assertEquals((BigDecimal.valueOf(11)), scoringService.determineMarStatus(scoredRate));
    }

    @Test
    void determineMarStatusShouldBeWIDOW_WIDOWER() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
      given(scoringDataDTO.getMaritalStatus()).willReturn(MaritalStatus.WIDOW_WIDOWER);
        assertEquals((BigDecimal.valueOf(10)), scoringService.determineMarStatus(scoredRate));
    }

    @Test
    void determineMarStatusShouldBeSINGLE() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getMaritalStatus()).willReturn(MaritalStatus.SINGLE);
        assertEquals((BigDecimal.valueOf(10)), scoringService.determineMarStatus(scoredRate));
    }



    @Test
    void determineGenderShouldBeMALEAndOLD() {
    // "MALE & OLD" means that he is about 35 - 55 y.o
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getBirthdate()).willReturn(LocalDate.of(1968,02,10));
        given(scoringDataDTO.getGender()).willReturn(Gender.MALE);
        assertEquals(BigDecimal.valueOf(7), scoringService.determineGender(scoredRate));
    }

    @Test
    void determineGenderShouldBeFEMALEAndYoung() {
        // "MALE & YOUNG" means that he is about 18 - 34 y.o
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getBirthdate()).willReturn(LocalDate.of(1998,02,10));
        given(scoringDataDTO.getGender()).willReturn(Gender.MALE);
        assertEquals(BigDecimal.valueOf(10), scoringService.determineGender(scoredRate));
    }


    @Test
    void determineGenderShouldBeFEMALEAndOLD() {
        // "FEMALE & OLD" means that she is about 35 - 60 y.o
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getBirthdate()).willReturn(LocalDate.of(1968,02,10));
        given(scoringDataDTO.getGender()).willReturn(Gender.FEMALE);
        assertEquals(BigDecimal.valueOf(7), scoringService.determineGender(scoredRate));
    }


    @Test
    void determineGenderShouldBeFEMALEAndYOUNG() {
        // "FEMALE & YOUNG" means that she is about 18 - 34 y.o
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getBirthdate()).willReturn(LocalDate.of(1998,02,10));
        given(scoringDataDTO.getGender()).willReturn(Gender.FEMALE);
        assertEquals(BigDecimal.valueOf(10), scoringService.determineGender(scoredRate));
    }


    @Test
    void determineGenderShouldBeNOTBINARY() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getBirthdate()).willReturn(LocalDate.of(1998,02,10));
        given(scoringDataDTO.getGender()).willReturn(Gender.NOTBINARY);
        assertEquals(BigDecimal.valueOf(13), scoringService.determineGender(scoredRate));

    }

    @Test
    void calculateMonthlyRate(){
        given(creditDTO.getRate()).willReturn(BigDecimal.valueOf(10));
        assertEquals(BigDecimal.valueOf(0.0083), scoringService.calculateMonthlyRate());
    }

    @Test
    void calculateCreditMonthlyPayment() {
        assertEquals(BigDecimal.valueOf(46100000, 4),
                scoringService.calculateCreditMonthlyPayment(
                        BigDecimal.valueOf(100000),
                        BigDecimal.valueOf(0.0083),
                        24) );
        assertEquals(BigDecimal.valueOf(10860000, 4 ),
                scoringService.calculateCreditMonthlyPayment(
                        BigDecimal.valueOf(30000),
                        BigDecimal.valueOf(0.015),
                        36) );

        assertEquals(BigDecimal.valueOf(28200000, 4),
                scoringService.calculateCreditMonthlyPayment(
                        BigDecimal.valueOf(50000),
                        BigDecimal.valueOf(0.0015),
                        18) );

    }

    @Test
    void calculatePsk() {
        given(creditDTO.getRate()).willReturn(BigDecimal.valueOf(12));
        assertEquals(BigDecimal.valueOf(80000, 4),
                scoringService.calculatePsk(3,
                BigDecimal.valueOf(100000)));
    }

    @Test
    void calculateSchedule() {
    }
}
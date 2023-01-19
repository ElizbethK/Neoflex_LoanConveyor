package ru.neostudy.loanConveyorProject.conveyor.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.neostudy.loanConveyorProject.conveyor.dto.EmploymentDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;
import ru.neostudy.loanConveyorProject.conveyor.enums.Position;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class ScoringServiceTest {


    @Mock
    private ScoringDataDTO scoringDataDTO;
    @Mock
    private EmploymentDTO employmentDTO;

    private ScoringService scoringService;


    public ScoringServiceTest() {
        MockitoAnnotations.openMocks(this);
        this.scoringService = new ScoringService(scoringDataDTO, employmentDTO);
    }

    @Test
    void score() {
    }

    @Test
    void determineStatusJob() {
    }

    @Test
    void determinePositionShouldBeMANAGER() throws Exception {
        given(employmentDTO.getPosition()).willReturn(Position.MID_MANAGER);
        assertEquals(BigDecimal.valueOf(8), scoringService.determinePosition(BigDecimal.valueOf(10)));
    }

    @Test
    void determinePositionShouldBeTOPMANAGER() {
        given(this.employmentDTO.getPosition()).willReturn(Position.TOPMANAGER);
        assertEquals(BigDecimal.valueOf(6), scoringService.determinePosition(BigDecimal.valueOf(10)));
    }

    @Test
    void determinePositionShouldBeLABORER() {
        given(this.employmentDTO.getPosition()).willReturn(Position.WORKER);
        assertEquals(BigDecimal.valueOf(10), scoringService.determinePosition(BigDecimal.valueOf(10)));
    }


    @Test
    void determineMarStatus() {
    }

    @Test
    void determineGender() {

    }

    @Test
    void calculateCreditMonthlyPayment() {
    }

    @Test
    void calculatePsk() {
    }

    @Test
    void calculateSchedule() {
    }
}
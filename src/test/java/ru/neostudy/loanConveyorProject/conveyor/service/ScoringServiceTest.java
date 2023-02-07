package ru.neostudy.loanConveyorProject.conveyor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neostudy.loanConveyorProject.conveyor.dto.CreditDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.EmploymentDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.PaymentScheduleElement;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;
import ru.neostudy.loanConveyorProject.conveyor.enums.EmploymentStatus;
import ru.neostudy.loanConveyorProject.conveyor.enums.Gender;
import ru.neostudy.loanConveyorProject.conveyor.enums.MaritalStatus;
import ru.neostudy.loanConveyorProject.conveyor.enums.Position;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static ru.neostudy.loanConveyorProject.conveyor.enums.EmploymentStatus.BUSINESSOWNER;
import static ru.neostudy.loanConveyorProject.conveyor.enums.Gender.MALE;
import static ru.neostudy.loanConveyorProject.conveyor.enums.MaritalStatus.DIVORCED;
import static ru.neostudy.loanConveyorProject.conveyor.enums.MaritalStatus.MARRIED;
import static ru.neostudy.loanConveyorProject.conveyor.enums.Position.OWNER;


@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {

    @Mock
    private EmploymentDTO employmentDTO;
    @Mock
    private CreditDTO creditDTO;
    @Mock
    private ScoringDataDTO scoringDataDTO;


    @InjectMocks
    private ScoringService scoringService;




    @Test
    void scoreApplied() {
        BigDecimal amount = new BigDecimal(100086);
        int term = 24;

        EmploymentStatus employmentStatus = BUSINESSOWNER;
        Position position = OWNER;
        BigDecimal salary = new BigDecimal(55000);
        Integer workExperienceTotal = 35;
        Integer workExperienceCurrent = 6;
        MaritalStatus maritalStatus = DIVORCED;
        int dependentAmount = 3;
        LocalDate birthday = LocalDate.of(1967, 12, 11);
        Gender gender = MALE;
        BigDecimal monthlyPayment = new BigDecimal("4714.0506");
        BigDecimal rate = new BigDecimal(12);
        BigDecimal psk = new BigDecimal("6.5200");
        List<PaymentScheduleElement> paymentSchedule;


        employmentDTO = new EmploymentDTO();
        employmentDTO.setEmploymentStatus(employmentStatus);
        employmentDTO.setPosition(position);
        employmentDTO.setSalary(salary);
        employmentDTO.setWorkExperienceTotal(workExperienceTotal);
        employmentDTO.setWorkExperienceCurrent(workExperienceCurrent);



        when(scoringDataDTO.getAmount()).thenReturn(amount);
        when(scoringDataDTO.getTerm()).thenReturn(term);
        when(scoringDataDTO.getEmployment()).thenReturn(employmentDTO);
        when(scoringDataDTO.getMaritalStatus()).thenReturn(maritalStatus);
        when(scoringDataDTO.getBirthdate()).thenReturn(birthday);
        when(scoringDataDTO.getGender()).thenReturn(gender);
        when(scoringDataDTO.getDependentAmount()).thenReturn(dependentAmount);


        CreditDTO creditDTO1 = scoringService.score(scoringDataDTO);
        assertEquals(amount, creditDTO1.getAmount());
        assertEquals(term, creditDTO1.getTerm());
        assertEquals(monthlyPayment, creditDTO1.getMonthlyPayment());
        assertEquals(rate, creditDTO1.getRate());
        assertEquals(psk, creditDTO1.getPsk());
        paymentSchedule = creditDTO1.getPaymentSchedule();
        assertEquals(term, paymentSchedule.size());
        assertEquals("CREDIT APPROVED", creditDTO1.getCreditDecision());

    }

   /* @Test
    void scoreRejected() {
        BigDecimal amount = new BigDecimal(100086);
        int term = 24;

        EmploymentStatus employmentStatus = BUSINESSOWNER;
        Position position = OWNER;
        BigDecimal salary = new BigDecimal(55000);
        Integer workExperienceTotal = 8;
        Integer workExperienceCurrent = 1;
        MaritalStatus maritalStatus = DIVORCED;
        int dependentAmount = 3;
        LocalDate birthday = LocalDate.of(1967, 12, 11);
        Gender gender = MALE;


        employmentDTO = new EmploymentDTO();
        employmentDTO.setEmploymentStatus(employmentStatus);
        employmentDTO.setPosition(position);
        employmentDTO.setSalary(salary);
        employmentDTO.setWorkExperienceTotal(workExperienceTotal);
        employmentDTO.setWorkExperienceCurrent(workExperienceCurrent);


        when(scoringDataDTO.getAmount()).thenReturn(amount);
        when(scoringDataDTO.getTerm()).thenReturn(term);
        when(scoringDataDTO.getEmployment()).thenReturn(employmentDTO);
        when(scoringDataDTO.getMaritalStatus()).thenReturn(maritalStatus);
        when(scoringDataDTO.getBirthdate()).thenReturn(birthday);
        when(scoringDataDTO.getGender()).thenReturn(gender);
        when(scoringDataDTO.getDependentAmount()).thenReturn(dependentAmount);


        CreditDTO creditDTO1 = scoringService.score(scoringDataDTO);
        assertEquals("CREDIT DECLINED", creditDTO1.getCreditDecision());

    }
*/

    @Test
    void determineStatusJobShouldBeEMPLOYED() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(employmentDTO.getEmploymentStatus()).willReturn(EmploymentStatus.EMPLOYED);
        assertEquals(BigDecimal.valueOf(10), scoringService.determineStatusJob(scoredRate, employmentDTO));
    }

    @Test
    void determineStatusJobShouldBeUNEMPLOYED() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(employmentDTO.getEmploymentStatus()).willReturn(EmploymentStatus.UNEMPLOYED);
        assertEquals(BigDecimal.valueOf(10), scoringService.determineStatusJob(scoredRate, employmentDTO));
    }

    @Test
    void determineStatusJobShouldBeSELFEMPLOYED() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(employmentDTO.getEmploymentStatus()).willReturn(EmploymentStatus.SELFEMPLOYED);
        assertEquals(BigDecimal.valueOf(11), scoringService.determineStatusJob(scoredRate, employmentDTO));
    }

    @Test
    void determineStatusJobShouldBeBUSINESSOWNER() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(employmentDTO.getEmploymentStatus()).willReturn(BUSINESSOWNER);
        assertEquals(BigDecimal.valueOf(13), scoringService.determineStatusJob(scoredRate, employmentDTO));
    }


    @Test
    void determinePositionShouldBeMANAGER() throws Exception {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(employmentDTO.getPosition()).willReturn(Position.MID_MANAGER);
        assertEquals(BigDecimal.valueOf(8), scoringService.determinePosition(scoredRate, employmentDTO));
    }

    @Test
    void determinePositionShouldBeTOPMANAGER() throws Exception{
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(this.employmentDTO.getPosition()).willReturn(Position.TOPMANAGER);
        assertEquals(BigDecimal.valueOf(6), scoringService.determinePosition(scoredRate, employmentDTO));
    }

    @Test
    void determinePositionShouldBeLABORER() throws Exception{
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(this.employmentDTO.getPosition()).willReturn(Position.WORKER);
        assertEquals(BigDecimal.valueOf(10), scoringService.determinePosition(scoredRate, employmentDTO));
    }


    @Test
    void determineMarStatusShouldBeMARRIED() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getMaritalStatus()).willReturn(MARRIED);
        assertEquals((BigDecimal.valueOf(7)), scoringService.determineMarStatus(scoredRate, scoringDataDTO));
    }

    @Test
    void determineMarStatusShouldBeDIVORCED() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getMaritalStatus()).willReturn(DIVORCED);
        assertEquals((BigDecimal.valueOf(11)), scoringService.determineMarStatus(scoredRate, scoringDataDTO));
    }

    @Test
    void determineMarStatusShouldBeWIDOW_WIDOWER() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
      given(scoringDataDTO.getMaritalStatus()).willReturn(MaritalStatus.WIDOW_WIDOWER);
        assertEquals((BigDecimal.valueOf(10)), scoringService.determineMarStatus(scoredRate, scoringDataDTO));
    }

    @Test
    void determineMarStatusShouldBeSINGLE() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getMaritalStatus()).willReturn(MaritalStatus.SINGLE);
        assertEquals((BigDecimal.valueOf(10)), scoringService.determineMarStatus(scoredRate, scoringDataDTO));
    }


    @Test
    void determineGenderShouldBeMALEAndOLD() {
    // "MALE & OLD" means that he is about 35 - 55 y.o
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getBirthdate()).willReturn(LocalDate.of(1968,02,10));
        given(scoringDataDTO.getGender()).willReturn(MALE);
        assertEquals(BigDecimal.valueOf(7), scoringService.determineGender(scoredRate, scoringDataDTO));
    }

    @Test
    void determineGenderShouldBeFEMALEAndYoung() {
        // "MALE & YOUNG" means that he is about 18 - 34 y.o
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getBirthdate()).willReturn(LocalDate.of(1998,02,10));
        given(scoringDataDTO.getGender()).willReturn(MALE);
        assertEquals(BigDecimal.valueOf(10), scoringService.determineGender(scoredRate, scoringDataDTO));
    }


    @Test
    void determineGenderShouldBeFEMALEAndOLD() {
        // "FEMALE & OLD" means that she is about 35 - 60 y.o
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getBirthdate()).willReturn(LocalDate.of(1968,02,10));
        given(scoringDataDTO.getGender()).willReturn(Gender.FEMALE);
        assertEquals(BigDecimal.valueOf(7), scoringService.determineGender(scoredRate, scoringDataDTO));
    }


    @Test
    void determineGenderShouldBeFEMALEAndYOUNG() {
        // "FEMALE & YOUNG" means that she is about 18 - 34 y.o
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getBirthdate()).willReturn(LocalDate.of(1998,02,10));
        given(scoringDataDTO.getGender()).willReturn(Gender.FEMALE);
        assertEquals(BigDecimal.valueOf(10), scoringService.determineGender(scoredRate,scoringDataDTO));
    }


    @Test
    void determineGenderShouldBeNOTBINARY() {
        BigDecimal scoredRate = BigDecimal.valueOf(10);
        given(scoringDataDTO.getBirthdate()).willReturn(LocalDate.of(1998,02,10));
        given(scoringDataDTO.getGender()).willReturn(Gender.NOTBINARY);
        assertEquals(BigDecimal.valueOf(13), scoringService.determineGender(scoredRate, scoringDataDTO));
    }

    @Test
    void calculateMonthlyRate(){
        when(creditDTO.getRate()).thenReturn(BigDecimal.valueOf(10));
        assertEquals(BigDecimal.valueOf(0.0083), scoringService.calculateMonthlyRate(creditDTO));
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
        when(creditDTO.getRate()).thenReturn(BigDecimal.valueOf(12));
        assertEquals(BigDecimal.valueOf(80000, 4),
                scoringService.calculatePsk(3,
                BigDecimal.valueOf(100000), creditDTO));
    }
    @Test
    void calculateSchedule() {
        when(creditDTO.getTerm()).thenReturn(24);
        when(creditDTO.getAmount()).thenReturn(BigDecimal.valueOf(100070));
        when(creditDTO.getMonthlyPayment()).thenReturn(BigDecimal.valueOf(1087));
        when(creditDTO.getRate()).thenReturn(BigDecimal.valueOf(10));

        List<PaymentScheduleElement> paymentScheduleElements = scoringService.calculateSchedule(creditDTO);
        assertEquals(24, scoringService.calculateSchedule(creditDTO).size());
    }
}
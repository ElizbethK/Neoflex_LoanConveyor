package ru.neostudy.loanConveyorProject.conveyor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.CreditDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.EmploymentDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.PaymentScheduleElement;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;
import ru.neostudy.loanConveyorProject.conveyor.enums.EmploymentStatus;
import ru.neostudy.loanConveyorProject.conveyor.enums.Gender;
import ru.neostudy.loanConveyorProject.conveyor.enums.MaritalStatus;
import ru.neostudy.loanConveyorProject.conveyor.enums.Position;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


@Service
public class ScoringService{

    @Value("${baseRate}")
    BigDecimal baseRate;

    private EmploymentStatus employmentStatus;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Position position;
    private static BigDecimal scoredRate;
    private static Boolean decision = true;



    private CreditDTO creditDTO = new CreditDTO();

    @Autowired
    private EmploymentDTO employmentDTO;

    @Autowired
    private ScoringDataDTO scoringDataDTO;

    public ScoringService(ScoringDataDTO scoringDataDTO, EmploymentDTO employmentDTO) {
        this.scoringDataDTO = scoringDataDTO;
        this.employmentDTO = employmentDTO;
        this.scoredRate = baseRate;

    }


    //-----------------------------------------------------------

    public CreditDTO score() {
        creditDTO.setAmount(scoringDataDTO.getAmount());
        creditDTO.setTerm(scoringDataDTO.getTerm());


        while (decision = true) {

//Рабочий статус: Безработный → отказ; Самозанятый → ставка увеличивается на 1; Владелец бизнеса → ставка увеличивается на 3

            if(String.valueOf(this.employmentDTO.getEmploymentStatus()).equals( "UNEMPLOYED")){
                System.out.println("The loan application was refused");
                decision = false;
            } else
                scoredRate = determineStatusJob(scoredRate);
            creditDTO.setRate(scoredRate);



//Позиция на работе: Менеджер среднего звена → ставка уменьшается на 2; Топ-менеджер → ставка уменьшается на 4
            scoredRate = determinePosition(scoredRate);
            creditDTO.setRate(scoredRate);



//Сумма займа больше, чем 20 зарплат → отказ
            if((scoringDataDTO.getAmount()).compareTo((employmentDTO.getSalary()).multiply(BigDecimal.valueOf(20))) > 0){
                System.out.println("The loan application was refused");
                decision = false;
            }


//Семейное положение: Замужем/женат → ставка уменьшается на 3; Разведен → ставка увеличивается на 1
            scoredRate = determineMarStatus(scoredRate);
            creditDTO.setRate(scoredRate);


// Количество иждивенцев больше 1 → ставка увеличивается на 1
            if(scoringDataDTO.getDependentAmount() > 1){
                scoredRate = scoredRate.add(BigDecimal.valueOf(1));
                creditDTO.setRate(scoredRate);
            }


//Возраст менее 20 или более 60 лет → отказ
            int age = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();
            if(age < 20 || age > 60 ){
                System.out.println("The loan application was refused");
                decision = false;
            }

//Пол:
//Женщина, возраст от 35 до 60 лет → ставка уменьшается на 3;
//Мужчина, возраст от 30 до 55 лет → ставка уменьшается на 3;
//Не бинарный → ставка увеличивается на 3
            scoredRate = determineGender(scoredRate);
            creditDTO.setRate(scoredRate);


//Стаж работы:
//Общий стаж менее 12 месяцев → отказ;
//Текущий стаж менее 3 месяцев → отказ
            if (employmentDTO.getWorkExperienceTotal().compareTo(12) < 0){
                System.out.println("The loan application was refused");
                decision = false;
            }

            if(employmentDTO.getWorkExperienceCurrent().compareTo(3) < 0){
                System.out.println("The loan application was refused");
                decision = false;
            }

//Высчитать итоговую  ставку:
            creditDTO.setRate(scoredRate);
            System.out.println("scoredRate = creditDTO.getRate = " + creditDTO.getRate());

//Высчитать размер ежемесячного платежа(monthlyPayment):
            BigDecimal monthlyRate = ((creditDTO.getRate().divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(12)));
            BigDecimal monthlyPayment = calculateCreditMonthlyPayment(creditDTO.getAmount(), monthlyRate, creditDTO.getTerm());
            creditDTO.setMonthlyPayment(monthlyPayment);

//Высчитать полную стоимость кредита(psk):
            creditDTO.setPsk(calculatePsk(creditDTO.getTerm(), creditDTO.getAmount(),
                    creditDTO.getMonthlyPayment()));

// Высчитать график ежемесячных платежей (List<PaymentScheduleElement>):
            creditDTO.setPaymentSchedule(calculateSchedule());

        } return creditDTO;
    }


//-----------------------------------------------


    public BigDecimal determineStatusJob(BigDecimal scoredRate){
        String status = String.valueOf(this.employmentDTO.getEmploymentStatus());

        switch (status) {
            case "SELFEMPLOYED":
                scoredRate = (baseRate.add(BigDecimal.valueOf(1)));
                break;
            case "BUSINESSOWNER":
                scoredRate = (baseRate.add(BigDecimal.valueOf(3)));
                break;
            default:
                scoredRate = scoredRate;
                break;
        } return scoredRate;
    }


    public BigDecimal determinePosition(BigDecimal scoredRate){
        String position = String.valueOf(this.employmentDTO.getPosition());
        switch (position) {
            case "MID_MANAGER":
                scoredRate = (scoredRate.subtract(BigDecimal.valueOf(2)));
                break;
            case "TOPMANAGER":
                scoredRate = (scoredRate.subtract(BigDecimal.valueOf(4)));
                break;
            default:
                scoredRate = scoredRate;
                break;
        } return scoredRate;

    }

    public BigDecimal determineMarStatus(BigDecimal scoredRate){
        if((this.scoringDataDTO.getMaritalStatus()).equals(MaritalStatus.MARRIED)){
            scoredRate = (scoredRate.subtract(BigDecimal.valueOf(3)));
        } else if ((this.scoringDataDTO.getMaritalStatus()).equals(MaritalStatus.DIVORCED)){
            scoredRate = (scoredRate.add(BigDecimal.valueOf(1)));
        } else  scoredRate = creditDTO.getRate();
        return scoredRate;
    }

    //1 вариант
    public BigDecimal determineGender(BigDecimal scoredRate){
        int age = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();

        if((this.scoringDataDTO.getGender()).equals(Gender.MALE)) {
            if (age > 35 & age < 60) {
                scoredRate = (scoredRate.subtract(BigDecimal.valueOf(3)));
            } else scoredRate = scoredRate;
        }

        else if ((this.scoringDataDTO.getGender()).equals(Gender.FEMALE)) {
            if (age > 30 & age < 55) {
                scoredRate = (scoredRate.subtract(BigDecimal.valueOf(3)));
            } else scoredRate = scoredRate;
        }

        else {
            scoredRate = (scoredRate.add(BigDecimal.valueOf(3)));
        }
        return scoredRate;
    }


   /* // 2 вариант
    public BigDecimal determineGender(BigDecimal scoredRate){
        int age = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();

        Enum g = scoringDataDTO.getGender();

        if((g == (Gender.MALE))) {
            if (age > 35 & age < 60) {
                scoredRate = (scoredRate.subtract(BigDecimal.valueOf(3)));
            } else scoredRate = scoredRate;
        }

        else if (g == (Gender.FEMALE)) {
            if (age > 30 & age < 55) {
                scoredRate = (scoredRate.subtract(BigDecimal.valueOf(3)));
            } else scoredRate = scoredRate;
        }

        else {
            scoredRate = (scoredRate.add(BigDecimal.valueOf(3)));
        }
        return scoredRate;
    }*/




    public BigDecimal calculateCreditMonthlyPayment(BigDecimal amount, BigDecimal monthlyRate, Integer term){
        BigDecimal numerator = monthlyRate.multiply((monthlyRate.add(BigDecimal.valueOf(1))).pow(term));
        BigDecimal denominator = ((monthlyRate.add(BigDecimal.valueOf(1))).pow(term)).subtract(BigDecimal.valueOf(1));
        BigDecimal monthPay = (numerator.divide(denominator)).multiply(amount);
        return  monthPay;
    }

    public BigDecimal calculatePsk(Integer term, BigDecimal amount, BigDecimal monthlyPayment){
        BigDecimal s = monthlyPayment.multiply(BigDecimal.valueOf(term));
        BigDecimal numerator = (s.divide(amount)).subtract(BigDecimal.valueOf(1));
        BigDecimal denominator = BigDecimal.valueOf(term / 12);
        BigDecimal psk = (numerator.divide(denominator)).multiply(BigDecimal.valueOf(100));
        return psk;
    }

    public List<PaymentScheduleElement> calculateSchedule(){
        List<PaymentScheduleElement> paymentScheduleElementList = new ArrayList<>();

        LocalDate nowDate = LocalDate.now();

        BigDecimal remainingD = creditDTO.getAmount();
        BigDecimal totalPay = creditDTO.getMonthlyPayment();
        BigDecimal interestPay;
        BigDecimal debtPay;


        for(int i = 1; i <= creditDTO.getTerm(); i++) {
            PaymentScheduleElement paymentScheduleElement = new PaymentScheduleElement();

            //number
            paymentScheduleElement.setNumber(i);

            //date
            nowDate = nowDate.plusMonths(1);
            paymentScheduleElement.setDate(nowDate);

            //totalPayment = constant
            paymentScheduleElement.setTotalPayment(totalPay);

            //interestPayment
            BigDecimal denominator = BigDecimal.valueOf(nowDate.lengthOfMonth()).divide(BigDecimal.valueOf(nowDate.lengthOfYear()));
            interestPay = remainingD.multiply((creditDTO.getRate()).divide(BigDecimal.valueOf(100))).multiply(denominator);
            paymentScheduleElement.setInterestPayment(interestPay);

            //debtPayment
            debtPay = totalPay.subtract(interestPay);
            paymentScheduleElement.setDebtPayment(debtPay);

            //remainingDebt
            remainingD = remainingD.subtract(debtPay);
            paymentScheduleElement.setRemainingDebt(remainingD);

            paymentScheduleElementList.add(paymentScheduleElement);
        }
        for (PaymentScheduleElement p:paymentScheduleElementList
        ) {
            System.out.println(p);
        }
        return paymentScheduleElementList;
    }


}

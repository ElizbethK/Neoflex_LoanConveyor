package ru.neostudy.loanConveyorProject.conveyor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.CreditDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.EmploymentDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.PaymentScheduleElement;
import ru.neostudy.loanConveyorProject.conveyor.dto.ScoringDataDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.enums.EmploymentStatus;
import ru.neostudy.loanConveyorProject.conveyor.dto.enums.Gender;
import ru.neostudy.loanConveyorProject.conveyor.dto.enums.MaritalStatus;
import ru.neostudy.loanConveyorProject.conveyor.dto.enums.Position;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ScoringService {

    @Value("${baseRate}")
    BigDecimal baseRate;

    private CreditDTO creditDTO = new CreditDTO();
    private EmploymentStatus employmentStatus;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Position position;

    @Autowired
    private EmploymentDTO employmentDTO;

    @Autowired
    private ScoringDataDTO scoringDataDTO;

    public ScoringService(ScoringDataDTO scoringDataDTO) {
        this.scoringDataDTO = scoringDataDTO;
    }

    public CreditDTO score() {
        BigDecimal scoredRate = baseRate;
        creditDTO.setAmount(scoringDataDTO.getAmount());
        creditDTO.setTerm(scoringDataDTO.getTerm());

        Boolean decision = true;
        while (decision = true) {

//Рабочий статус: Безработный → отказ; Самозанятый → ставка увеличивается на 1; Владелец бизнеса → ставка увеличивается на 3
            String status = String.valueOf(employmentDTO.getEmploymentStatus());
            switch (status) {
                case "UNEMPLOYED":
                    System.out.println("The loan application was refused");
                    decision = false;
                    break;
                case "FREELANCER":
                    creditDTO.setRate(baseRate.add(BigDecimal.valueOf(1)));
                    scoredRate = creditDTO.getRate();
                    break;
                case "ENTERPRENEUR":
                    creditDTO.setRate(baseRate.add(BigDecimal.valueOf(3)));
                    scoredRate = creditDTO.getRate();
                    break;
                default:
                    creditDTO.setRate(baseRate);
                    scoredRate = creditDTO.getRate();
                    break;
            }

//Позиция на работе: Менеджер среднего звена → ставка уменьшается на 2; Топ-менеджер → ставка уменьшается на 4
            String position = String.valueOf(employmentDTO.getPosition());

            switch (position) {
                case "MANAGER":
                    creditDTO.setRate(scoredRate.subtract(BigDecimal.valueOf(3)));
                    scoredRate = creditDTO.getRate();
                    break;
                case "TOPMANAGER":
                    creditDTO.setRate(scoredRate.subtract(BigDecimal.valueOf(4)));
                    scoredRate = creditDTO.getRate();
                    break;
                default:
                    scoredRate = creditDTO.getRate();
                    break;
            }

//Сумма займа больше, чем 20 зарплат → отказ
            if(scoringDataDTO.getAmount().compareTo((employmentDTO.getSalary()).multiply(BigDecimal.valueOf(20))) > 0){
                System.out.println("The loan application was refused");
                decision = false;
            }


//Семейное положение: Замужем/женат → ставка уменьшается на 3; Разведен → ставка увеличивается на 1
            String marStatus = String.valueOf(scoringDataDTO.getMaritalStatus());
            switch (marStatus){
                case "MARRIED":
                    creditDTO.setRate(scoredRate.subtract(BigDecimal.valueOf(3)));
                    scoredRate = creditDTO.getRate();
                    break;
                case "DIVORCED":
                    creditDTO.setRate(scoredRate.add(BigDecimal.valueOf(1)));
                    scoredRate = creditDTO.getRate();
                    break;
                default:
                    scoredRate = creditDTO.getRate();
                    break;
            }

// Количество иждивенцев больше 1 → ставка увеличивается на 1
            if(scoringDataDTO.getDependentAmount() > 1){
                creditDTO.setRate(scoredRate.add(BigDecimal.valueOf(1)));
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
            String gender = String.valueOf(scoringDataDTO.getGender());
            if(gender.equals("MALE")) {
                if (age > 35 & age < 60) {
                    creditDTO.setRate(scoredRate.subtract(BigDecimal.valueOf(3)));
                    scoredRate = creditDTO.getRate();
                } else continue;
            }
            else if (gender.equals("FEMALE")) {
                    if (age > 30 & age < 55) {
                        creditDTO.setRate(scoredRate.subtract(BigDecimal.valueOf(3)));
                        scoredRate = creditDTO.getRate();
                    } else continue;
            }
               else if (gender.equals("NOTBINARY")){
                creditDTO.setRate(scoredRate.add(BigDecimal.valueOf(3)));
                scoredRate = creditDTO.getRate();
            } else continue;


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


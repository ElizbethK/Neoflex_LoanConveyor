package ru.neostudy.loanConveyorProject.deal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.PaymentScheduleElement;
import ru.neostudy.loanConveyorProject.deal.controller.DealController;
import ru.neostudy.loanConveyorProject.deal.entity.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AttachmentsService {
    private static final Logger logger = LoggerFactory.getLogger(DealController.class);


    public void buildCreditApplicationFile(Application application){
        logger.info("Запущен метод buildCreditApplicationFile(Application application). {} ", application);
        Client client = application.getClient();
        logger.info("Извлечен {}", client);
        String lastName = client.getLastName();
        String firstName = client.getFirstName();
        String middleName = client.getMiddleName();

        Passport passport = application.getClient().getPassport();
        logger.info("Извлечен {}", passport);
        String series = passport.getSeries();
        String number = passport.getNumber();

        String birthDate = String.valueOf(client.getBirthDate());
        String gender = String.valueOf(client.getGender());
        String email = client.getEmail();

        try(FileWriter writer = new FileWriter("credit-application.txt", false))
        {
            String text = "-- Application --" + "\n" +
                    "Last name: " + lastName + "\n" +
                    "First name: " + firstName +  "\n" +
                    "Middle name: " +  middleName + "\n" +
                    "Passport series: " + series + "\n" +
                    "Passport number: " + number + "\n" +
                    "Birth date: " + birthDate + "\n" +
                    "Gender: " + gender + "\n" +
                    "Email: " + email + "\n";
            writer.write(text);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        logger.info("Credit-application.txt created");
    }


    public void buildCreditLoanFile(Application application){
        logger.info("Запущен метод buildCreditLoanFile(Application application). {} ", application);
        Credit credit = application.getCredit();
        logger.info("Извлечен {}", credit);
        BigDecimal amount = credit.getAmount();
        int term = credit.getTerm();
        BigDecimal monthlyPayment = credit.getMonthlyPayment();
        BigDecimal rate = credit.getRate();
        BigDecimal psk = credit.getPsk();
        String insuranceEnable = String.valueOf(credit.isInsuranceEnable());
        String salaryClient = String.valueOf(credit.isSalaryClient());
        String creditStatus = String.valueOf(credit.getCreditStatus());


        try(FileWriter writer = new FileWriter("credit-loan.txt", false))
        {
            String text = "-- Credit --" + "\n" +
                    "Credit amount: " + amount + "\n" +
                    "Term: " + term +  "\n" +
                    "Monthly payment: " +  monthlyPayment + "\n" +
                    "Rate: " + rate + "\n" +
                    "Psk: " + psk + "\n" +
                    "Insurance enable: " + insuranceEnable + "\n" +
                    "Salary client: " + salaryClient + "\n" +
                    "Credit status: " + creditStatus + "\n";
            writer.write(text);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        logger.info("Credit-loan.txt created");
    }


    public void buildCreditPaymentScheduleFile(Application application){
        logger.info("Запущен метод buildCreditPaymentScheduleFile(Application application). {} ", application);
        Credit credit = application.getCredit();
        logger.info("Извлечен {}", credit);
        List<PaymentScheduleElement> paymentSchedule = credit.getPaymentSchedule();

        try(FileWriter writer = new FileWriter("credit-schedule.txt", false))
        {
            String text = "-- Payment schedule --" + "\n" +
                    paymentSchedule;
            writer.write(text);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        logger.info("Credit-schedule.txt created");
    }

}

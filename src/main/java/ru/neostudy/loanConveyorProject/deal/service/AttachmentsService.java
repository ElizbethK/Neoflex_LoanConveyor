package ru.neostudy.loanConveyorProject.deal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.deal.entity.Application;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.entity.Employment;
import ru.neostudy.loanConveyorProject.deal.entity.Passport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class AttachmentsService {

    @Autowired
    ClientService clientService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    ScoringDataDTOService scoringDataDTOService;

    public void buildCreditApplicationFile(Application application){
        Client client = application.getClient();
        String lastName = client.getLastName();
        String firstName = client.getFirstName();
        String middleName = client.getMiddleName();

        Passport passport = application.getClient().getPassport();
        String series = passport.getSeries();
        String number = passport.getNumber();

        String birthDate = String.valueOf(client.getBirthDate());

        String gender = String.valueOf(client.getGender());
        String email = client.getEmail();


        try(FileWriter writer = new FileWriter("credit-application.txt", false))
        {
            // запись всей строки
            String text = "--Credit Application--" + "\n" +
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
    }

  /*  public FileWriter buildCreditLoanFile(Application application){

    }*/

   /* public FileWriter buildCreditPaymentScheduleFile(Application application){

    }*/

}

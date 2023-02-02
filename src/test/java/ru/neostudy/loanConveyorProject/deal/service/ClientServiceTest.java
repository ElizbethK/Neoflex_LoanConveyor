package ru.neostudy.loanConveyorProject.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.repository.ClientRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void createClient() {
        BigDecimal amount1 =  BigDecimal.valueOf(123836);
        int term1 = 24;
        String lastName1 = "Smirnov";
        String firstName1 = "Alexey";
        String middleName1 = "Igorevich";
        String email1 = "djhbj@kjfn.ru";
        LocalDate birthdate1 = LocalDate.of(1996, 04, 03);
        String passportSeries1 = "2212";
        String passportNumber1 = "234567";

        BigDecimal amount2 =  BigDecimal.valueOf(10006);
        int term2 = 43;
        String lastName2 = "Banks";
        String firstName2 = "Jim";
        String middleName2 = "John";
        String email2 = "jimBanks@kjfn.ru";
        LocalDate birthdate2 = LocalDate.of(1945, 11, 17);
        String passportSeries2 = "2345";
        String passportNumber2 = "200487";


        LoanApplicationRequestDTO loanApplicationRequestDTO1 =
                new LoanApplicationRequestDTO( amount1, term1, lastName1,
                        firstName1, middleName1, email1,birthdate1,passportSeries1, passportNumber1);

        LoanApplicationRequestDTO loanApplicationRequestDTO2 =
                new LoanApplicationRequestDTO( amount2, term2, lastName2,
                        firstName2, middleName2, email2,birthdate2,passportSeries2, passportNumber2);

        List<Client> clientList;
        clientList = List.of(
                clientService.createClient(loanApplicationRequestDTO1),
                clientService.createClient(loanApplicationRequestDTO2)
        );

        assertEquals(lastName1, clientList.get(0).getLastName());
        assertEquals(firstName1, clientList.get(0).getFirstName());
        assertEquals(middleName1, clientList.get(0).getMiddleName());
        assertEquals(email1, clientList.get(0).getEmail());
        assertEquals(birthdate1, clientList.get(0).getBirthDate());


        assertEquals(lastName2, clientList.get(1).getLastName());
        assertEquals(firstName2, clientList.get(1).getFirstName());
        assertEquals(middleName2, clientList.get(1).getMiddleName());
        assertEquals(email2, clientList.get(1).getEmail());
        assertEquals(birthdate2, clientList.get(1).getBirthDate());


    }



    @Test
    void createClient2() {
        BigDecimal amount1 =  BigDecimal.valueOf(123836);
        int term1 = 24;
        String lastName1 = "Smirnov";
        String firstName1 = "Alexey";
        String middleName1 = "Igorevich";
        String email1 = "djhbj@kjfn.ru";
        LocalDate birthdate1 = LocalDate.of(1996, 04, 03);
        String passportSeries1 = "2212";
        String passportNumber1 = "234567";

        BigDecimal amount2 =  BigDecimal.valueOf(10006);
        int term2 = 43;
        String lastName2 = "Banks";
        String firstName2 = "Jim";
        String middleName2 = "John";
        String email2 = "jimBanks@kjfn.ru";
        LocalDate birthdate2 = LocalDate.of(1945, 11, 17);
        String passportSeries2 = "2345";
        String passportNumber2 = "200487";


        LoanApplicationRequestDTO loanApplicationRequestDTO1 =
                new LoanApplicationRequestDTO( amount1, term1, lastName1,
                        firstName1, middleName1, email1,birthdate1,passportSeries1, passportNumber1);

        LoanApplicationRequestDTO loanApplicationRequestDTO2 =
                new LoanApplicationRequestDTO( amount2, term2, lastName2,
                        firstName2, middleName2, email2,birthdate2,passportSeries2, passportNumber2);

        List<Client> clientList;
        clientList = List.of(
                clientService.createClient(loanApplicationRequestDTO1),
                clientService.createClient(loanApplicationRequestDTO2),
                clientService.createClient(loanApplicationRequestDTO1),
                clientService.createClient(loanApplicationRequestDTO2),
                clientService.createClient(loanApplicationRequestDTO2)
        );





        Client client1 = clientList.get(0);
        Client client2 = clientList.get(1);

        verify(clientRepository,times(2)).save(client1);
        verify(clientRepository,times(3)).save(client2);

        assertEquals(5, clientList.size());
    }

}


package ru.neostudy.loanConveyorProject.deal.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;

import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.repository.ClientRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    ClientRepository clientRepository;

    @InjectMocks
    ClientService clientService;

    public ClientServiceTest(){
        this.clientService = new ClientService(clientRepository);
    }

    @Test
    void createClient() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();
        loanApplicationRequestDTO.setLastName("Mikiev");
        loanApplicationRequestDTO.setFirstName("Alex");
        loanApplicationRequestDTO.setMiddleName("Joe");
        loanApplicationRequestDTO.setBirthdate(LocalDate.parse("1999-06-10"));
        loanApplicationRequestDTO.setEmail("jhgf12@gmail.com");

        List<Client> clientList = new ArrayList<>();
        clientList.add(clientService.createClient(loanApplicationRequestDTO));
        Assertions.assertEquals(1, clientList.size());

    }
}
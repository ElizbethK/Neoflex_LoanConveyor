package ru.neostudy.loanConveyorProject.deal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Client;
import ru.neostudy.loanConveyorProject.deal.repository.ClientRepository;

@Service
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(LoanApplicationRequestDTO loanApplicationRequestDTO){
        logger.info("Создание Client из: {}", loanApplicationRequestDTO);
        Client client = new Client();
        client.setLastName(loanApplicationRequestDTO.getLastName());
        client.setFirstName(loanApplicationRequestDTO.getFirstName());
        client.setMiddleName(loanApplicationRequestDTO.getMiddleName());
        client.setBirthDate(loanApplicationRequestDTO.getBirthdate());
        client.setEmail(loanApplicationRequestDTO.getEmail());
        logger.info("Client создан: {}", client);
        clientRepository.save(client);
        logger.info("Client сохранен. {}", client);
        return client;
    }
}

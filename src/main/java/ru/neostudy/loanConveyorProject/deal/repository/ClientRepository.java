package ru.neostudy.loanConveyorProject.deal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

}

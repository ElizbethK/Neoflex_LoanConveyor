package ru.neostudy.loanConveyorProject.deal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.neostudy.loanConveyorProject.deal.entity.Application;
import ru.neostudy.loanConveyorProject.deal.entity.Client;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {


}

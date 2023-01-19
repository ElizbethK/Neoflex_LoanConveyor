package ru.neostudy.loanConveyorProject.deal.entity;

import lombok.Data;
import ru.neostudy.loanConveyorProject.conveyor.enums.Gender;
import ru.neostudy.loanConveyorProject.conveyor.enums.MaritalStatus;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id", nullable = false)
    private long clientId;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "marital_status", nullable = false)
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount", nullable = false)
    private int dependentAmount;

    //Foreign Key
    @Column(name = "passport_id", nullable = false)
    private long passportId;

    //Foreign Key
    @Column(name = "employment_id", nullable = false)
    private long employmentId;

    @Column(name = "account", nullable = false)
    private String account;


}

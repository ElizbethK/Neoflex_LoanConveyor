package ru.neostudy.loanConveyorProject.deal.entity;

import lombok.Data;
import ru.neostudy.loanConveyorProject.conveyor.enums.EmploymentStatus;
import ru.neostudy.loanConveyorProject.conveyor.enums.Position;

import javax.persistence.*;
import java.math.BigDecimal;


@Data
@Entity
@Table(name = "employment")
public class Employment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employment_id", nullable = false)
    private long employmentId;

    @Column(name = "employment_status")
    private EmploymentStatus employmentStatus;

    @Column(name = "employer_inn")
    private String employerINN;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "position")
    private Position position;

    @Column(name = "work_experience_total")
    private Integer workExperienceTotal;

    @Column(name = "work_experience_current")
    private Integer workExperienceCurrent;
}

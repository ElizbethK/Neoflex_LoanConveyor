package ru.neostudy.loanConveyorProject.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;
import ru.neostudy.loanConveyorProject.conveyor.enums.EmploymentStatus;
import ru.neostudy.loanConveyorProject.conveyor.enums.Position;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Data
@Entity
@Table(name = "employment")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Employment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employment_id", nullable = false)
    private long employmentId;

    //Enum
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status")
    private EmploymentStatus employmentStatus;


    @Column(name = "employer_inn")
    private String employerINN;


    @Column(name = "salary")
    private BigDecimal salary;


    //Enum
    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;


    @Column(name = "work_experience_total")
    private Integer workExperienceTotal;


    @Column(name = "work_experience_current")
    private Integer workExperienceCurrent;



    //1to1
    @OneToOne(mappedBy = "employment")
    private Client client;
}

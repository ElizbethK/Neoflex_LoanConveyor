package ru.neostudy.loanConveyorProject.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;
import ru.neostudy.loanConveyorProject.conveyor.enums.Gender;
import ru.neostudy.loanConveyorProject.conveyor.enums.MaritalStatus;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Data
@Entity
@Table(name = "client")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id", nullable = false)
    private long clientId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;


    @Column(name = "middle_name")
    private String middleName;


    @Column(name = "birth_date")
    private LocalDate birthDate;


    @Column(name = "email")
    private String email;

    //Enum
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    //Enum
    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;


    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    //Foreign Key
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id", referencedColumnName = "passport_id")
    private Passport passport;

    //Foreign Key
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_id", referencedColumnName = "employment_id")
    private Employment employment;

    @Column(name = "account")
    private String account;



    //1to1
    @OneToOne(mappedBy = "client")
    private Application application;


}

package ru.neostudy.loanConveyorProject.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.neostudy.loanConveyorProject.conveyor.dto.PaymentScheduleElement;
import ru.neostudy.loanConveyorProject.deal.enums.CreditStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Data
@Entity
@Table(name = "credit")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "credit_id", nullable = false)
    private long creditId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "term")
    private int term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "psk")
    private BigDecimal psk;

    //Jsonb
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<PaymentScheduleElement> paymentSchedule;


    @Column(name = "insurance_enable")
    private boolean insuranceEnable;


    @Column(name = "salary_client")
    private boolean salaryClient;


    //Enum
    @Enumerated(EnumType.STRING)
    @Column(name = "credit_status")
    private CreditStatus creditStatus;



    //1to1
    @OneToOne(mappedBy = "credit")
    private Application application;
}

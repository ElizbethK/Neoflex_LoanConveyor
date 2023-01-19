package ru.neostudy.loanConveyorProject.deal.entity;

import lombok.Data;
import org.hibernate.annotations.Type;
import ru.neostudy.loanConveyorProject.conveyor.dto.PaymentScheduleElement;
import ru.neostudy.loanConveyorProject.conveyor.enums.CreditStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "credit")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "application_id", nullable = false)
    private long creditId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "term", nullable = false)
    private int term;

    @Column(name = "monthly_payment", nullable = false)
    private BigDecimal monthlyPayment;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

    @Column(name = "psk", nullable = false)
    private BigDecimal psk;


    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<PaymentScheduleElement> paymentSchedule;


    @Column(name = "insurance_enable", nullable = false)
    private boolean insuranceEnable;

    @Column(name = "salary_client", nullable = false)
    private boolean salaryClient;

    @Column(name = "credit_status", nullable = false)
    private CreditStatus creditStatus;

}

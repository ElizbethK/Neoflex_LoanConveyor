package ru.neostudy.loanConveyorProject.deal.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class PaymentScheduleJsonb implements Serializable {

    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;  //Сумма платежа
    private BigDecimal interestPayment; //выплата процентов (платеж по процентам)
    private BigDecimal debtPayment; // Выплата долга (платеж по основному долгу)
    private BigDecimal remainingDebt; //Оставшийся долг (остаток долга)
}

package ru.neostudy.loanConveyorProject.conveyor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PaymentScheduleElement {
    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;  //Сумма платежа
    private BigDecimal interestPayment; //выплата процентов (платеж по процентам)
    private BigDecimal debtPayment; // Выплата долга (платеж по основному долгу)
    private BigDecimal remainingDebt; //Оставшийся долг (остаток долга)


}

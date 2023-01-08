package ru.neostudy.loanConveyorProject.conveyor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PaymentScheduleElement {
    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;

    public PaymentScheduleElement(Integer number, LocalDate date, BigDecimal totalPayment,
                                  BigDecimal interestPayment, BigDecimal debtPayment,
                                  BigDecimal remainingDebt) {
        this.number = number;
        this.date = date;
        this.totalPayment = totalPayment;
        this.interestPayment = interestPayment;
        this.debtPayment = debtPayment;
        this.remainingDebt = remainingDebt;
    }
}

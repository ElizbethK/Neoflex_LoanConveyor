package ru.neostudy.loanConveyorProject.conveyor.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreditDTO {
    @NotEmpty(message = "should not be empty")
    private BigDecimal amount;

    @NotEmpty(message = "should not be empty")
    private Integer term;

    @NotEmpty(message = "should not be empty")
    private BigDecimal monthlyPayment;

    @NotEmpty(message = "should not be empty")
    private BigDecimal rate;

    @NotEmpty(message = "should not be empty")
    private BigDecimal psk;

    @NotEmpty(message = "should not be empty")
    private Boolean isInsuranceEnabled;

    @NotEmpty(message = "should not be empty")
    private Boolean isSalaryClient;

    @NotEmpty(message = "should not be empty")
    private List<PaymentScheduleElement> paymentSchedule;

    public CreditDTO(BigDecimal amount, Integer term, BigDecimal monthlyPayment,
                     BigDecimal rate, BigDecimal psk, Boolean isInsuranceEnabled,
                     Boolean isSalaryClient, List<PaymentScheduleElement> paymentSchedule) {
        this.amount = amount;
        this.term = term;
        this.monthlyPayment = monthlyPayment;
        this.rate = rate;
        this.psk = psk;
        this.isInsuranceEnabled = isInsuranceEnabled;
        this.isSalaryClient = isSalaryClient;
        this.paymentSchedule = paymentSchedule;
    }
}

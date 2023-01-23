package ru.neostudy.loanConveyorProject.conveyor.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class CreditDTO {

    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElement> paymentSchedule;





}

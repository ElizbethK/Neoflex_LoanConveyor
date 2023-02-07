package ru.neostudy.loanConveyorProject.conveyor.dto;

import lombok.*;


import java.math.BigDecimal;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class LoanOfferDTO {


    private Long applicationId;

    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

    public LoanOfferDTO() {
        this.applicationId = Long.valueOf((int)(1 + Math.random() * 999));
    }
}



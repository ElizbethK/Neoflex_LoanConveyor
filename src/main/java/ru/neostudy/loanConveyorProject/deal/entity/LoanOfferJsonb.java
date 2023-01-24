package ru.neostudy.loanConveyorProject.deal.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Data
public class LoanOfferJsonb implements Serializable {

    private Long applicationId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

    public LoanOfferJsonb() {
        this.applicationId = (long)(1 + Math.random() * 999);
    }


}

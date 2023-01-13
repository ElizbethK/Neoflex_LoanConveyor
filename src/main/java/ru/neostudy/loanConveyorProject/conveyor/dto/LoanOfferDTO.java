package ru.neostudy.loanConveyorProject.conveyor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.math.BigDecimal;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoanOfferDTO {

    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)*/
    private Long applicationId;

    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;



}



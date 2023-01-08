package ru.neostudy.loanConveyorProject.conveyor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


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

    @Value("${baseRate}")
    BigDecimal baseRate;

    private LoanApplicationRequestDTO loanApplicationRequestDTO;

    @Autowired
    public LoanOfferDTO(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        this.loanApplicationRequestDTO = loanApplicationRequestDTO;
    }


    public LoanOfferDTO createOfferDTO(Boolean isInsuranceEnabled, Boolean isSalaryClient){
        LoanOfferDTO loanOffersVariety = new LoanOfferDTO();
        setRequestedAmount(loanApplicationRequestDTO.getAmount());
        setTerm(loanApplicationRequestDTO.getTerm());


        if(!(isInsuranceEnabled) & !(isInsuranceEnabled)){
            setTotalAmount(loanApplicationRequestDTO.getAmount());     // + 0%
            setRate(baseRate);                                        // - 0%
            setIsInsuranceEnabled(false);
            setIsSalaryClient(false);

            BigDecimal monthlyRate = ((getRate().divide(BigDecimal.valueOf(12))).multiply(BigDecimal.valueOf(100)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(getTerm()))));
            setMonthlyPayment(getRequestedAmount().multiply((monthlyRate.divide(denominator))));


        }
        else if(!(isInsuranceEnabled) & isSalaryClient){
            setTotalAmount(loanApplicationRequestDTO.getAmount());  // + 0% salary client
            setRate(baseRate.subtract(BigDecimal.valueOf(1)));      // - 1%
            setIsInsuranceEnabled(false);
            setIsSalaryClient(true);

            BigDecimal monthlyRate = ((getRate().divide(BigDecimal.valueOf(12))).multiply(BigDecimal.valueOf(100)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(getTerm()))));
            setMonthlyPayment(getRequestedAmount().multiply((monthlyRate.divide(denominator))));


        }
        else if(isInsuranceEnabled & !(isSalaryClient)){
            setTotalAmount((loanApplicationRequestDTO.getAmount()).multiply(new BigDecimal(1.1))); // +10% insurance
            setRate(baseRate.subtract(BigDecimal.valueOf(3)));                                         // - 3%
            setIsInsuranceEnabled(true);
            setIsSalaryClient(false);

            BigDecimal monthlyRate = ((getRate().divide(BigDecimal.valueOf(12))).multiply(BigDecimal.valueOf(100)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(getTerm()))));
            setMonthlyPayment(getRequestedAmount().multiply((monthlyRate.divide(denominator))));

        }
        else{
            setTotalAmount((loanApplicationRequestDTO.getAmount()).multiply(new BigDecimal(1.1))); // +10% insurance + 0% salary cl.
            setRate(baseRate.subtract(BigDecimal.valueOf(4)));                                        // - (1% + 3%)
            setIsInsuranceEnabled(true);
            setIsSalaryClient(true);

            BigDecimal monthlyRate = ((getRate().divide(BigDecimal.valueOf(12))).multiply(BigDecimal.valueOf(100)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(getTerm()))));
            setMonthlyPayment(getRequestedAmount().multiply((monthlyRate.divide(denominator))));
        }

        return loanOffersVariety;

    }



}



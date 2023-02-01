package ru.neostudy.loanConveyorProject.conveyor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanOfferService {

    @Value("${baseRate}")
    BigDecimal baseRate;


    @Autowired
    private Application application;



    public List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanRequest){
        return List.of(
                 createOfferDTO(false, false, loanRequest),
                 createOfferDTO(false, true, loanRequest),
                 createOfferDTO(true, false, loanRequest),
                 createOfferDTO(true, true, loanRequest)
        );
    }


    public  LoanOfferDTO createOfferDTO(Boolean isInsuranceEnabled, Boolean isSalaryClient,
                                        LoanApplicationRequestDTO loanRequest){
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setApplicationId(application.getApplicationId());

        loanOfferDTO.setRequestedAmount(loanRequest.getAmount());
        loanOfferDTO.setTerm(loanRequest.getTerm());
        loanOfferDTO.setIsSalaryClient(isSalaryClient);
        loanOfferDTO.setIsInsuranceEnabled(isInsuranceEnabled);


        if(!(isInsuranceEnabled) & !(isInsuranceEnabled)){
            loanOfferDTO.setTotalAmount(loanRequest.getAmount());     // + 0%
            loanOfferDTO.setRate(baseRate);                                        // - 0%
            loanOfferDTO.setMonthlyPayment(calculateMonthPayment(loanOfferDTO));


        }
        else if(!(isInsuranceEnabled) & isSalaryClient){
            loanOfferDTO.setTotalAmount(loanRequest.getAmount());  // + 0% salary client
            loanOfferDTO.setRate(baseRate.subtract(BigDecimal.valueOf(1)));      // - 1%
            loanOfferDTO.setMonthlyPayment(calculateMonthPayment(loanOfferDTO));


        }
        else if(isInsuranceEnabled & !(isSalaryClient)){
            loanOfferDTO.setTotalAmount((loanRequest.getAmount()).multiply(new BigDecimal(1.1))); // +10% insurance
            loanOfferDTO.setRate(baseRate.subtract(BigDecimal.valueOf(3)));                                         // - 3%
            loanOfferDTO.setMonthlyPayment(calculateMonthPayment(loanOfferDTO));
        }
        else{
            loanOfferDTO.setTotalAmount((loanRequest.getAmount()).multiply(new BigDecimal(1.1))); // +10% insurance + 0% salary cl.
            loanOfferDTO.setRate(baseRate.subtract(BigDecimal.valueOf(4)));                                        // - (1% + 3%)
            loanOfferDTO.setMonthlyPayment(calculateMonthPayment(loanOfferDTO));

        }

        return loanOfferDTO;


    }

    public BigDecimal calculateMonthPayment(LoanOfferDTO loanOfferDTO){
        BigDecimal monthlyRate = (((loanOfferDTO.getRate()).divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(12)));
        BigDecimal denominator = (new BigDecimal(1)).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(loanOfferDTO.getTerm()))));
        BigDecimal calculatedMonthlPay = (loanOfferDTO.getRequestedAmount().multiply((monthlyRate.divide(denominator))));
        return calculatedMonthlPay;
    }




}

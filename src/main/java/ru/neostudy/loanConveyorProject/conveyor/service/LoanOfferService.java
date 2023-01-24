package ru.neostudy.loanConveyorProject.conveyor.service;

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
public class LoanOfferService {

    @Value("${baseRate}")
    BigDecimal baseRate;

    private LoanOfferDTO loanOfferDTO;
    private List<LoanOfferDTO> loanOfferDTOList = new ArrayList<>();

    @Autowired
    private Application application;

    @Autowired
    private LoanApplicationRequestDTO loanApplicationRequestDTO;

    public LoanOfferService(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        this.loanApplicationRequestDTO = loanApplicationRequestDTO;
    }

    public List<LoanOfferDTO> createLoanOffers(){
        createOfferDTO(false, false);
        createOfferDTO(false, true);
        createOfferDTO(true, false);
        createOfferDTO(true, true);

        for (LoanOfferDTO l:this.loanOfferDTOList) {
            System.out.println(l);
        }
        return this.loanOfferDTOList;
    }


    public  List<LoanOfferDTO> createOfferDTO(Boolean isInsuranceEnabled, Boolean isSalaryClient){
        loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setApplicationId(application.getApplicationId());

        loanOfferDTO.setRequestedAmount(loanApplicationRequestDTO.getAmount());
        loanOfferDTO.setTerm(loanApplicationRequestDTO.getTerm());


        if(!(isInsuranceEnabled) & !(isInsuranceEnabled)){
            loanOfferDTO.setTotalAmount(loanApplicationRequestDTO.getAmount());     // + 0%
            loanOfferDTO.setRate(baseRate);                                        // - 0%
            loanOfferDTO.setMonthlyPayment(calculateMonthPayment(loanOfferDTO));

            loanOfferDTOList.add(loanOfferDTO);

        }
        else if(!(isInsuranceEnabled) & isSalaryClient){
            loanOfferDTO.setTotalAmount(loanApplicationRequestDTO.getAmount());  // + 0% salary client
            loanOfferDTO.setRate(baseRate.subtract(BigDecimal.valueOf(1)));      // - 1%
            loanOfferDTO.setMonthlyPayment(calculateMonthPayment(loanOfferDTO));

            loanOfferDTOList.add(loanOfferDTO);

        }
        else if(isInsuranceEnabled & !(isSalaryClient)){
            loanOfferDTO.setTotalAmount((loanApplicationRequestDTO.getAmount()).multiply(new BigDecimal(1.1))); // +10% insurance
            loanOfferDTO.setRate(baseRate.subtract(BigDecimal.valueOf(3)));                                         // - 3%
            loanOfferDTO.setMonthlyPayment(calculateMonthPayment(loanOfferDTO));

            loanOfferDTOList.add(loanOfferDTO);
        }
        else{
            loanOfferDTO.setTotalAmount((loanApplicationRequestDTO.getAmount()).multiply(new BigDecimal(1.1))); // +10% insurance + 0% salary cl.
            loanOfferDTO.setRate(baseRate.subtract(BigDecimal.valueOf(4)));                                        // - (1% + 3%)
            loanOfferDTO.setMonthlyPayment(calculateMonthPayment(loanOfferDTO));

            loanOfferDTOList.add(loanOfferDTO);
        }

        return loanOfferDTOList;

    }

    public BigDecimal calculateMonthPayment(LoanOfferDTO loanOfferDTO){
        BigDecimal monthlyRate = (((loanOfferDTO.getRate()).divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(12)));
        BigDecimal denominator = (new BigDecimal(1)).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(loanOfferDTO.getTerm()))));
        BigDecimal calculatedMonthlPay = (loanOfferDTO.getRequestedAmount().multiply((monthlyRate.divide(denominator))));
        return calculatedMonthlPay;
    }




}

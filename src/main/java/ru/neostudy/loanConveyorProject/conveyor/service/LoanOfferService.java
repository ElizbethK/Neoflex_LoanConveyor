package ru.neostudy.loanConveyorProject.conveyor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanOfferService {

    @Value("${baseRate}")
    BigDecimal baseRate;


    private LoanOfferDTO loanOffersVariety = new LoanOfferDTO();

    private List<LoanOfferDTO> loanOfferDTOList = new ArrayList<>();


    private LoanApplicationRequestDTO loanApplicationRequestDTO;
    @Autowired
    public LoanOfferService(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        this.loanApplicationRequestDTO = loanApplicationRequestDTO;
    }

    public List<LoanOfferDTO> createLoanOffers(){
       createOfferDTO(false, false, loanApplicationRequestDTO);
       createOfferDTO(false, true, loanApplicationRequestDTO);
       createOfferDTO(true, false, loanApplicationRequestDTO);
       createOfferDTO(true, true, loanApplicationRequestDTO);

        for (LoanOfferDTO l:this.loanOfferDTOList) {
            System.out.println(l);
        }
        return this.loanOfferDTOList;
    }


    public  List<LoanOfferDTO> createOfferDTO(Boolean isInsuranceEnabled, Boolean isSalaryClient,
                                              LoanApplicationRequestDTO loanApplicationRequestDTO){
        loanOffersVariety.setRequestedAmount(loanApplicationRequestDTO.getAmount());
        loanOffersVariety.setTerm(loanApplicationRequestDTO.getTerm());


        if(!(isInsuranceEnabled) & !(isInsuranceEnabled)){
            loanOffersVariety.setTotalAmount(loanApplicationRequestDTO.getAmount());     // + 0%
            loanOffersVariety.setRate(baseRate);                                        // - 0%

            BigDecimal monthlyRate = ((loanOffersVariety.getRate().divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(12)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(loanOffersVariety.getTerm()))));
            loanOffersVariety.setMonthlyPayment(loanOffersVariety.getRequestedAmount().multiply((monthlyRate.divide(denominator))));

            loanOfferDTOList.add(loanOffersVariety);

        }
        else if(!(isInsuranceEnabled) & isSalaryClient){
            loanOffersVariety.setTotalAmount(loanApplicationRequestDTO.getAmount());  // + 0% salary client
            loanOffersVariety.setRate(baseRate.subtract(BigDecimal.valueOf(1)));      // - 1%

            BigDecimal monthlyRate = ((loanOffersVariety.getRate().divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(12)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(loanOffersVariety.getTerm()))));
            loanOffersVariety.setMonthlyPayment(loanOffersVariety.getRequestedAmount().multiply((monthlyRate.divide(denominator))));

            loanOfferDTOList.add(loanOffersVariety);

        }
        else if(isInsuranceEnabled & !(isSalaryClient)){
            loanOffersVariety.setTotalAmount((loanApplicationRequestDTO.getAmount()).multiply(new BigDecimal(1.1))); // +10% insurance
            loanOffersVariety.setRate(baseRate.subtract(BigDecimal.valueOf(3)));                                         // - 3%

            BigDecimal monthlyRate = ((loanOffersVariety.getRate().divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(12)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(loanOffersVariety.getTerm()))));
            loanOffersVariety.setMonthlyPayment(loanOffersVariety.getRequestedAmount().multiply((monthlyRate.divide(denominator))));

            loanOfferDTOList.add(loanOffersVariety);
        }
        else{
            loanOffersVariety.setTotalAmount((loanApplicationRequestDTO.getAmount()).multiply(new BigDecimal(1.1))); // +10% insurance + 0% salary cl.
            loanOffersVariety.setRate(baseRate.subtract(BigDecimal.valueOf(4)));                                        // - (1% + 3%)

            BigDecimal monthlyRate = ((loanOffersVariety.getRate().divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(12)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(loanOffersVariety.getTerm()))));
            loanOffersVariety.setMonthlyPayment(loanOffersVariety.getRequestedAmount().multiply((monthlyRate.divide(denominator))));

            loanOfferDTOList.add(loanOffersVariety);
        }

        return loanOfferDTOList;

    }









    //---------------------------------

 /*   public List<LoanOfferDTO> createLoanOffers(LoanOfferDTO loanOfferDTO){
        loanOfferDTOList.add(loanOfferDTO.createOfferDTO(false, false));
        loanOfferDTOList.add(loanOfferDTO.createOfferDTO(false, true));
        loanOfferDTOList.add(loanOfferDTO.createOfferDTO(true, false));
        loanOfferDTOList.add(loanOfferDTO.createOfferDTO(true, true));

        for (LoanOfferDTO l:loanOfferDTOList) {
            System.out.println(l);
        }

        return loanOfferDTOList;

    }

*/


/*    public LoanOfferDTO createOfferDTO(Boolean isInsuranceEnabled, Boolean isSalaryClient){
        LoanOfferDTO loanOffersVariety = new LoanOfferDTO();
        loanOffersVariety.setRequestedAmount(loanApplicationRequestDTO.getAmount());
        loanOffersVariety.setTerm(loanApplicationRequestDTO.getTerm());


        if(!(isInsuranceEnabled) & !(isInsuranceEnabled)){
            setTotalAmount(loanApplicationRequestDTO.getAmount());     // + 0%
            setRate(baseRate);                                        // - 0%
//            setIsInsuranceEnabled(false);
//            setIsSalaryClient(false);

            BigDecimal monthlyRate = ((getRate().divide(BigDecimal.valueOf(12))).multiply(BigDecimal.valueOf(100)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(getTerm()))));
            setMonthlyPayment(getRequestedAmount().multiply((monthlyRate.divide(denominator))));


        }
        else if(!(isInsuranceEnabled) & isSalaryClient){
            setTotalAmount(loanApplicationRequestDTO.getAmount());  // + 0% salary client
            setRate(baseRate.subtract(BigDecimal.valueOf(1)));      // - 1%
//            setIsInsuranceEnabled(false);
//            setIsSalaryClient(true);

            BigDecimal monthlyRate = ((getRate().divide(BigDecimal.valueOf(12))).multiply(BigDecimal.valueOf(100)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(getTerm()))));
            setMonthlyPayment(getRequestedAmount().multiply((monthlyRate.divide(denominator))));


        }
        else if(isInsuranceEnabled & !(isSalaryClient)){
            setTotalAmount((loanApplicationRequestDTO.getAmount()).multiply(new BigDecimal(1.1))); // +10% insurance
            setRate(baseRate.subtract(BigDecimal.valueOf(3)));                                         // - 3%
//            setIsInsuranceEnabled(true);
//            setIsSalaryClient(false);

            BigDecimal monthlyRate = ((getRate().divide(BigDecimal.valueOf(12))).multiply(BigDecimal.valueOf(100)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(getTerm()))));
            setMonthlyPayment(getRequestedAmount().multiply((monthlyRate.divide(denominator))));

        }
        else{
            setTotalAmount((loanApplicationRequestDTO.getAmount()).multiply(new BigDecimal(1.1))); // +10% insurance + 0% salary cl.
            setRate(baseRate.subtract(BigDecimal.valueOf(4)));                                        // - (1% + 3%)
//            setIsInsuranceEnabled(true);
//            setIsSalaryClient(true);

            BigDecimal monthlyRate = ((getRate().divide(BigDecimal.valueOf(12))).multiply(BigDecimal.valueOf(100)));
            BigDecimal denominator = new BigDecimal(1).subtract((new BigDecimal(1).divide((new BigDecimal(1).add(monthlyRate)).pow(getTerm()))));
            setMonthlyPayment(getRequestedAmount().multiply((monthlyRate.divide(denominator))));
        }

        return loanOffersVariety;

    }*/



}

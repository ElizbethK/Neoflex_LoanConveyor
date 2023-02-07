package ru.neostudy.loanConveyorProject.conveyor.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanApplicationRequestDTO;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.deal.entity.Application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanOfferService {
    private static final Logger logger = LoggerFactory.getLogger(LoanOfferService.class);

    @Value("${baseRate}")
    BigDecimal baseRate = BigDecimal.valueOf(10);


    @Autowired
    private Application application = new Application();



    public List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanRequest){
        logger.info("Выполнен метод createLoanOffers, все loanRequest добавлены в лист");
        return List.of(
                 createOfferDTO(false, false, loanRequest),
                 createOfferDTO(false, true, loanRequest),
                 createOfferDTO(true, false, loanRequest),
                 createOfferDTO(true, true, loanRequest)
        );
    }


    private LoanOfferDTO createOfferDTO(Boolean isInsuranceEnabled, Boolean isSalaryClient,
                                        LoanApplicationRequestDTO loanRequest){
        logger.info("Запущен метод createOfferDTO с параметрами: " +
                "isInsuranceEnabled = {}, isSalaryClient = {}," +
                "amount = {}, term = {}, birthdate = {}",
                isInsuranceEnabled, isSalaryClient, loanRequest.getAmount(),
                loanRequest.getTerm(), loanRequest.getBirthdate());

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
        logger.info("Метод createOfferDTO завершен. Создан loanOfferDTO с параметрами: " +
                        "requestedAmount = {}, totalAmount = {}, term = {}, " +
                        "monthlyPayment = {}, rate = {}," +
                        "isInsuranceEnabled = {}, isSalaryClient = {}",
                loanOfferDTO.getRequestedAmount(), loanOfferDTO.getTotalAmount(), loanOfferDTO.getTerm(),
                loanOfferDTO.getMonthlyPayment(), loanOfferDTO.getRate(), loanOfferDTO.getIsInsuranceEnabled(),
                loanOfferDTO.getIsSalaryClient());

        return loanOfferDTO;

    }

    public BigDecimal calculateMonthPayment(LoanOfferDTO loanOfferDTO){
        BigDecimal monthlyRate = (((loanOfferDTO.getRate()).divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP));
        logger.info("Рассчитан monthlyRate = {}", monthlyRate);
        BigDecimal numerator = monthlyRate.multiply(((monthlyRate.add(BigDecimal.valueOf(1))).pow(loanOfferDTO.getTerm())));
        BigDecimal denominator = ((monthlyRate.add(BigDecimal.valueOf(1))).pow(loanOfferDTO.getTerm())).subtract(BigDecimal.valueOf(1));
        BigDecimal calculatedMonthlPay = (loanOfferDTO.getRequestedAmount().multiply((numerator.divide(denominator, 4, RoundingMode.HALF_UP))));
        logger.info("Рассчитан MonthlyPay = {}", calculatedMonthlPay);
        return calculatedMonthlPay;
    }




}

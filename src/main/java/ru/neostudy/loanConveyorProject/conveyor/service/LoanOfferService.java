package ru.neostudy.loanConveyorProject.conveyor.service;

import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import java.util.List;

@Service
public class LoanOfferService {


    private List<LoanOfferDTO> loanOfferDTOList;


    public List<LoanOfferDTO> createLoanOffers(LoanOfferDTO loanOfferDTO){
        loanOfferDTOList.add(loanOfferDTO.createOfferDTO(false, false));
        loanOfferDTOList.add(loanOfferDTO.createOfferDTO(false, true));
        loanOfferDTOList.add(loanOfferDTO.createOfferDTO(true, false));
        loanOfferDTOList.add(loanOfferDTO.createOfferDTO(true, true));

        for (LoanOfferDTO l:loanOfferDTOList) {
            System.out.println(l);
        }

        return loanOfferDTOList;

    }

}

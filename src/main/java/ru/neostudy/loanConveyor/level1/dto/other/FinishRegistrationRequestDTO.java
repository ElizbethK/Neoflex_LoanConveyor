package ru.neostudy.loanConveyor.level1.dto.other;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.neostudy.loanConveyor.level1.dto.lvl1.EmploymentDTO;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class FinishRegistrationRequestDTO {
    private Enum gender;
    private Enum maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBrach;
    private EmploymentDTO employment;
    private String account;

    public FinishRegistrationRequestDTO(Enum gender, Enum maritalStatus, Integer dependentAmount,
                                        LocalDate passportIssueDate, String passportIssueBrach,
                                        EmploymentDTO employment, String account) {
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.dependentAmount = dependentAmount;
        this.passportIssueDate = passportIssueDate;
        this.passportIssueBrach = passportIssueBrach;
        this.employment = employment;
        this.account = account;
    }
}

package ru.neostudy.loanConveyorProject.conveyor.dto.other;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.neostudy.loanConveyorProject.conveyor.dto.EmploymentDTO;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class FinishRegistrationRequestDTO {
    private Enum gender;
    private Enum maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDTO employment;
    private String account;

    public FinishRegistrationRequestDTO(Enum gender, Enum maritalStatus, Integer dependentAmount,
                                        LocalDate passportIssueDate, String passportIssueBrach,
                                        EmploymentDTO employment, String account) {
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.dependentAmount = dependentAmount;
        this.passportIssueDate = passportIssueDate;
        this.passportIssueBranch = passportIssueBrach;
        this.employment = employment;
        this.account = account;
    }
}

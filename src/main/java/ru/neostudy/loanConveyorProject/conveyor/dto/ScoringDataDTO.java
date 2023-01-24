package ru.neostudy.loanConveyorProject.conveyor.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.neostudy.loanConveyorProject.conveyor.enums.Gender;
import ru.neostudy.loanConveyorProject.conveyor.enums.MaritalStatus;


import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScoringDataDTO {
    @NotEmpty(message = "should not be empty")
    private BigDecimal amount;

    @NotEmpty(message = "should not be empty")
    private Integer term;

    @NotEmpty(message = "should not be empty")
    private String firstName;

    @NotEmpty(message = "should not be empty")
    private String lastName;

    private String middleName;


    private Gender gender;

    @NotEmpty(message = "should not be empty")
    private LocalDate birthdate;

    @Pattern(regexp = "\\d{4}", message = "this field must consist of 4 numbers")
    private String passportSeries;

    @Pattern(regexp = "\\d{4}", message = "this field must consist of 4 numbers")
    private String passportNumber;

    @NotEmpty(message = "should not be empty")
    private LocalDate passportIssueDate;

    @NotEmpty(message = "should not be empty")
    private String passportIssueBranch;


    private MaritalStatus maritalStatus;

    @NotEmpty(message = "should not be empty")
    private Integer dependentAmount;

    private EmploymentDTO employment;

    @NotEmpty(message = "should not be empty")
    private String account;

    @NotEmpty(message = "must be 'true' or 'false'")
    private Boolean isInsuranceEnabled;

    @NotEmpty(message = "must be 'true' or 'false'")
    private Boolean isSalaryClient;



}

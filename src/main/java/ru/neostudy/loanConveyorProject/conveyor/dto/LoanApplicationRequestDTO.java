package ru.neostudy.loanConveyorProject.conveyor.dto;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoanApplicationRequestDTO {

    @NotEmpty(message = "should not be empty")
    @Min(value = 10000, message = "the amount mustn`t be less than 10000")
    private BigDecimal amount;

    @NotEmpty(message = "should not be empty")
    @Min(value = 6, message = "the term mustn`t be less than 6" )
    @Pattern(regexp = "^[1-9]\\d*$")
    private Integer term;

    @NotEmpty(message = "should not be empty")
    @Pattern(regexp = "[a-zA-Z]{2,30}")
    private String firstName;

    @NotEmpty(message = "should not be empty")
    @Pattern(regexp = "[a-zA-Z]{2,30}")
    private String lastName;

    @Pattern(regexp = "[a-zA-Z]{0,30}")
    private String middleName;

    @Email
    @NotEmpty(message = "should not be empty")
    @Pattern(regexp = "[\\w.]{2,50}@[\\w.]{2,20}",
            message="Please provide a valid email address")
    private String email;

    @NotEmpty(message = "should not be empty")
    @Past(message="The birthday must be in the past")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    @Pattern(regexp = "^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$" ) // <- matches a date in yyyy-mm-dd format from 1900-01-01 through 2099-12-31, with a choice of four separators
    private LocalDate birthdate;

    @Pattern(regexp = "\\d{4}", message = "this field must consist of 4 numbers")
    private String passportSeries;

    @Pattern(regexp = "\\d{6}", message = "this field must consist of 6 numbers")
    private String passportNumber;

}

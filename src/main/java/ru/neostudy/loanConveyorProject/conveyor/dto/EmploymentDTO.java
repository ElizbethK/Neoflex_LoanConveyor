package ru.neostudy.loanConveyorProject.conveyor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmploymentDTO {

    private Enum employmentStatus;

    @NotEmpty(message = "should not be empty")
    private String employerINN;

    @NotEmpty(message = "should not be empty")
    private BigDecimal salary;


    private Enum position;

    @NotNull
    private Integer workExperienceTotal;

    @NotNull
    private Integer workExperienceCurrent;


}

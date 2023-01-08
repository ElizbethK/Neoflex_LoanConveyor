package ru.neostudy.loanConveyorProject.conveyor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import ru.neostudy.loanConveyorProject.conveyor.dto.enums.employmentStatus.EmploymentStatusPattern;
import ru.neostudy.loanConveyorProject.conveyor.dto.enums.gender.EnumGenderPattern;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmploymentDTO {
    @NotNull
    @EmploymentStatusPattern(regexp = " UNEMPLOYED|EMPLOYEE|FREELANCER|ENTERPRENEUR")
    private Enum employmentStatus;


    private String employerINN;


    private BigDecimal salary;

    @NotNull
    private Enum position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;


}

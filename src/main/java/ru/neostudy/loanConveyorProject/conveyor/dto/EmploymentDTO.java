package ru.neostudy.loanConveyorProject.conveyor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import ru.neostudy.loanConveyorProject.conveyor.dto.enums.employmentStatus.EmploymentStatusPattern;
import ru.neostudy.loanConveyorProject.conveyor.dto.enums.gender.EnumGenderPattern;
import ru.neostudy.loanConveyorProject.conveyor.dto.enums.position.EnumPositionPattern;

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
    @NotNull
    @EmploymentStatusPattern(regexp = "UNEMPLOYED|EMPLOYEE|FREELANCER|ENTERPRENEUR")
    private Enum employmentStatus;

    @NotEmpty(message = "should not be empty")
    @Pattern(regexp = "^[0-9]\\d{10,12}$")
    private String employerINN;

    @NotEmpty(message = "should not be empty")
    @Pattern(regexp = "^[1-9][0-9]*$")
    private BigDecimal salary;

    @NotNull
    @EnumPositionPattern(regexp = "LABORER|MANAGER|TOPMANAGER")
    private Enum position;

    @NotNull
    @Pattern(regexp = "^[1-9][0-9]*$")
    private Integer workExperienceTotal;

    @NotNull
    @Pattern(regexp = "^[1-9][0-9]*$")
    private Integer workExperienceCurrent;


}

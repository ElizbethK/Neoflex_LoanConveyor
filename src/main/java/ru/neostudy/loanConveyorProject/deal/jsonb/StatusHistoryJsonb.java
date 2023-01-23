package ru.neostudy.loanConveyorProject.deal.jsonb;

import lombok.*;
import ru.neostudy.loanConveyorProject.conveyor.enums.EmploymentStatus;
import ru.neostudy.loanConveyorProject.deal.enums.ChangeType;

import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StatusHistoryJsonb implements Serializable {
    private EmploymentStatus employmentStatus;
    private LocalDate time;
    private ChangeType changeType;
}

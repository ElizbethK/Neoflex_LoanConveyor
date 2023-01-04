package ru.neostudy.loanConveyor.level1.dto.other;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationStatusHistoryDTO {
    private Enum status;
    private LocalDateTime time;
    private Enum changeType;

    public ApplicationStatusHistoryDTO(Enum status, LocalDateTime time, Enum changeType) {
        this.status = status;
        this.time = time;
        this.changeType = changeType;
    }
}

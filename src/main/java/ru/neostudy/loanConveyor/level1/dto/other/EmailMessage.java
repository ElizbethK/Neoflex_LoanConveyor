package ru.neostudy.loanConveyor.level1.dto.other;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailMessage {
    private String address;
    private Enum theme;
    private Long applicationId;
}

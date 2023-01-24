package ru.neostudy.loanConveyorProject.conveyor.dto;

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

    public EmailMessage(String address, Enum theme, Long applicationId) {
        this.address = address;
        this.theme = theme;
        this.applicationId = applicationId;
    }
}

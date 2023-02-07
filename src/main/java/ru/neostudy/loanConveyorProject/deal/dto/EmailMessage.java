package ru.neostudy.loanConveyorProject.deal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailMessage {
    private String address;
    private Theme theme;
    private Long applicationId;
    private String message;
    private Object attachment;

    public EmailMessage(String address, Theme theme, Long applicationId, String message) {
        this.address = address;
        this.theme = theme;
        this.applicationId = applicationId;
        this.message = message;
    }

    public EmailMessage(String address, Theme theme, Long applicationId, String message, Object attachment) {
        this.address = address;
        this.theme = theme;
        this.applicationId = applicationId;
        this.message = message;
        this.attachment = attachment;
    }
}

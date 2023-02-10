package ru.neostudy.loanConveyorProject.dossier.service;

import ru.neostudy.loanConveyorProject.deal.dto.Theme;

import javax.mail.MessagingException;

public interface EmailSenderService {

    void sendEmail(String address, Theme theme, Long applicationId, String message);

    void sendEmailWithAttachment(String address, Theme theme, Long applicationId, String ...attachment) throws MessagingException;

}

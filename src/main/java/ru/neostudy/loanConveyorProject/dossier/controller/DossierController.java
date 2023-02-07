package ru.neostudy.loanConveyorProject.dossier.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neostudy.loanConveyorProject.deal.dto.EmailMessage;
import ru.neostudy.loanConveyorProject.dossier.service.EmailService;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/dossier")
public class DossierController {
     @Autowired
     private EmailService emailService;

    public DossierController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity sendEmail(@RequestBody EmailMessage emailMessage){
        emailService.sendEmail(emailMessage.getAddress(),
                emailMessage.getTheme(), emailMessage.getApplicationId(),
                emailMessage.getMessage());
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/send/attachment")
    public ResponseEntity sendEmailWithAttachment(@RequestBody EmailMessage emailMessage) throws MessagingException {
        emailService.sendEmailWithAttachment(emailMessage.getAddress(),
                emailMessage.getTheme(), emailMessage.getApplicationId(),
                String.valueOf(emailMessage.getAttachment()));
        return ResponseEntity.ok("Success");
    }
}

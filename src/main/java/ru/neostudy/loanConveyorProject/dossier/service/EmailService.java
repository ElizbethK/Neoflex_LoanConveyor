package ru.neostudy.loanConveyorProject.dossier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.neostudy.loanConveyorProject.deal.dto.Theme;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;


@Service
public class EmailService implements EmailSenderService{
    @Autowired
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String address, Theme theme, Long applicationId, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom("loanconveyor@gmail.com");
        simpleMailMessage.setTo(address);
        simpleMailMessage.setSubject(String.valueOf(theme));
        simpleMailMessage.setText(String.valueOf(applicationId));
        simpleMailMessage.setText(message);
         mailSender.send(simpleMailMessage);

    }

    @Override
    public void sendEmailWithAttachment(String address, Theme theme, Long applicationId,
                                        String ...attachments ) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("loanconveyor@gmail.com");
        mimeMessageHelper.setTo(address);
        mimeMessageHelper.setText(String.valueOf(applicationId));
        mimeMessageHelper.setSubject(String.valueOf(theme));

        for (String attachment : attachments
             ) { mimeMessageHelper.setText(attachment);
            FileSystemResource fileSystem
                    = new FileSystemResource(new File(attachment));
            mimeMessageHelper.addAttachment(fileSystem.getFilename(),
                    fileSystem);
        }
        mailSender.send(mimeMessage);
        System.out.println("Mail Send.");


    }
}

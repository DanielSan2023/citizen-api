package com.example.service;

import com.example.model.Document;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.logging.Logger;

import static com.example.utility.EmailConstants.*;

@ApplicationScoped
public class EmailService {

    @Resource(name = "mail/Session")
    private Session mailSession;

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    public void sendExpirationEmail(Document document) {
        try {
            Message message = new MimeMessage(mailSession);
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(document.getCitizen().getFirstName() + document.getCitizen().getLastName() + GMAIL_COM));
            message.setSubject(DOCUMENT_EXPIRATION_NOTIFICATION);
            message.setText(YOUR_DOCUMENT_OF_TYPE + document.getType() +
                    NUMBER + document.getNumber() + EXPIRES_ON +
                    document.getExpiryDate() + PLEASE_RENEW_IT_BEFORE_THE_EXPIRATION_DATE);

            Transport.send(message);
        } catch (MessagingException e) {
            LOGGER.severe("Failed to send email: " + e.getMessage());
        }
    }
}

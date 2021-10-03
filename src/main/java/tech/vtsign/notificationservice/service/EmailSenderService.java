package tech.vtsign.notificationservice.service;

import tech.vtsign.notificationservice.model.Mail;

import javax.mail.MessagingException;

public interface EmailSenderService {
    void sendEmail(Mail mail) throws MessagingException;
}

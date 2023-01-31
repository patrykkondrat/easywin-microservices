package com.easywin.notificationservice.mail;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSender {

    private final JavaMailSender mailSender;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);

        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }
}

package com.easywin.notificationservice.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailSenderTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    private EmailSender emailSender;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        emailSender = new EmailSender(mailSender);
    }

    @Test
    public void testSendMail() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        String to = "test@example.com";
        String subject = "Test subject";
        String text = "Test message";
        emailSender.sendMail(to, subject, text);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void shouldThrowExceptionIfMailSenderIsNull() {
        emailSender = new EmailSender(null);

        assertThrows(IllegalStateException.class, () -> emailSender.sendMail("test@example.com", "Test Subject", "Test email content"));

        verify(mailSender, never()).createMimeMessage();
    }

    @Test
    public void shouldRethrowRuntimeException() throws MessagingException {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test email content";
        RuntimeException messagingException = new RuntimeException("Test " +
                "runtime exception");

        when(mailSender.createMimeMessage()).thenThrow(messagingException);

        assertThrows(RuntimeException.class, () -> emailSender.sendMail(to, subject, text));

        verify(mailSender, times(1)).createMimeMessage();
    }
}
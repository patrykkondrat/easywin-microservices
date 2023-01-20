package com.easywin.notificationservice;

import com.easywin.notificationservice.event.TicketPlaceEvent;
import com.easywin.notificationservice.mail.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

//    private final EmailSender emailSender;
//    public NotificationServiceApplication(EmailSender emailSender) {
//        this.emailSender = emailSender;
//    }

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(TicketPlaceEvent ticketPlaceEvent) {
//        emailSender.sendSimpleMessage(ticketPlaceEvent.getEmail(),
//                "Ticket placed",
//                "Your ticket " + ticketPlaceEvent.getTicketNumber() + " has been placed");
        log.info("Received Notification for Ticket - {}", ticketPlaceEvent.getTicketNumber());
    }
}

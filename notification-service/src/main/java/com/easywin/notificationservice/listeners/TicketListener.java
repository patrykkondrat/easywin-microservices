package com.easywin.notificationservice.listeners;

import com.easywin.notificationservice.event.TicketPlaceEvent;
import com.easywin.notificationservice.mail.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;


@Component
@Slf4j
public class TicketListener {
    @Autowired
    private EmailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @KafkaListener(topics = "ticketTopic")
    public void handleTicket(TicketPlaceEvent ticketPlaceEvent) throws MessagingException {
        log.info("Received Notification for Ticket - {}", ticketPlaceEvent.getTicketNumber());

        Context ctx = new Context();
        ctx.setVariable("ticket", ticketPlaceEvent.getTicketLineItemsList());
        ctx.setVariable("ticket-number", ticketPlaceEvent.getTicketNumber());
        ctx.setVariable("total-stake", ticketPlaceEvent.getTotalStake());
        ctx.setVariable("winning", ticketPlaceEvent.getTotalWin());

        String body = templateEngine.process("ticketmail.html", ctx);

        emailSender.sendMail("dr34mer00@gmail.com", "Ticket Placed", body);
    }
}

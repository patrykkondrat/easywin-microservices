package com.easywin.notificationservice.listeners;

import com.easywin.notificationservice.event.TicketPlaceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TicketListener {
    @KafkaListener(topics = "ticketTopic")
    public void handleTicket(TicketPlaceEvent ticketPlaceEvent) {
        log.info("Received Notification for Ticket - {}", ticketPlaceEvent.getTicketNumber());
    }
}

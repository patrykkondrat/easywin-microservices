package com.easywin.notificationservice.listeners;

import com.easywin.notificationservice.event.PromoEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PromoListener {
    @KafkaListener(topics = "promoTopic")
    public void handlePromo(PromoEvent promo) {
        log.info("Received Notification for Promo - {}", promo.getName());
    }
}

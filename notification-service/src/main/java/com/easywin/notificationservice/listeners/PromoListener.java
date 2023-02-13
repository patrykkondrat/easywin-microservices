package com.easywin.notificationservice.listeners;

import com.easywin.notificationservice.event.PromoEvent;
import com.easywin.notificationservice.mail.EmailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Component
@Slf4j
public class PromoListener {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;


    @KafkaListener(topics = "promoTopic")
    public void handlePromo(PromoEvent promo) throws MessagingException {
        log.info("Received Notification for Promo - {}", promo.getName());

        Context ctx = new Context();
        ctx.setVariable("name", promo.getName());
        ctx.setVariable("organizator", promo.getOrganizer());

        final String body = templateEngine.process("ticketmail.html", ctx);

        emailSender.sendMail("dr34mer00@gmail.com", "Promo Event", body);
    }
}

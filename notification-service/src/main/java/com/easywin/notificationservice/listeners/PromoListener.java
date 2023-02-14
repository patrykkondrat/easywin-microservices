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
        ctx.setVariable("organizer", promo.getOrganizer());
        ctx.setVariable("description", promo.getDescription());
        ctx.setVariable("startDate", promo.getStartDate());
        ctx.setVariable("endDate", promo.getEndDate());
        ctx.setVariable("startTime", promo.getStartTime());
        ctx.setVariable("endTime", promo.getEndTime());
        ctx.setVariable("location", promo.getLocation());

        final String body = templateEngine.process("promomail.html", ctx);

        emailSender.sendMail("dr34mer00@gmail.com", "Promo Event", body); // TODO: change to user email (maybe should be all users or group of users)
    }
}

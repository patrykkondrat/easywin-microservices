package com.easywin.notificationservice.config;


import com.easywin.notificationservice.event.TicketPlaceEvent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {

    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }
    @Bean
    ConsumerFactory<String, TicketPlaceEvent> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
//        properties.put("bootstrap.servers", "broker:9092");
        properties.put("group.id", "group");
        properties.put("enable.auto.commit", false);
        properties.put("auto.commit.interval.ms", "10");
        properties.put("session.timeout.ms", "60000");
        properties.put("spring.json.trusted.packages", "*");
        properties.put("key.deserializer.class.config", StringDeserializer.class);
        properties.put("value.deserializer.class.config", JsonDeserializer.class);
        properties.put(JsonDeserializer.TYPE_MAPPINGS, "event:com.easywin.notificationservice.event.TicketPlaceEvent");
        ErrorHandlingDeserializer<TicketPlaceEvent> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(new JsonDeserializer<>(TicketPlaceEvent.class));
        return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(), errorHandlingDeserializer);
    }

    @Bean
    KafkaListenerContainerFactory<?> kafkaListenerContainerFactory(ConsumerFactory<String, TicketPlaceEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, TicketPlaceEvent> kafkaListenerContainerFactory
                = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConcurrency(2);
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        kafkaListenerContainerFactory.setErrorHandler(new KafkaErrorHandler());
        return kafkaListenerContainerFactory;
    }

}

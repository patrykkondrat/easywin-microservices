package com.easywin.promoservice.service;

import com.easywin.promoservice.dto.EventRequest;
import com.easywin.promoservice.dto.EventResponse;
import com.easywin.promoservice.event.PromoEvent;
import com.easywin.promoservice.model.Event;
import com.easywin.promoservice.model.EventStatus;
import com.easywin.promoservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;

    private final KafkaTemplate<String, PromoEvent> kafkaTemplate;

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(this::mapToDto).toList();
    }


    public EventResponse getEvent(String id) {
        return eventRepository.findById(id).map(this::mapToDto).orElseThrow(EntityNotFoundException::new);
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    public void createEvent(EventRequest eventRequest) {
        Event event = new Event();
        event.setId(UUID.randomUUID().toString());
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setStartDate(eventRequest.getStartDate());
        event.setEndDate(eventRequest.getEndDate());
        event.setStartTime(eventRequest.getStartTime());
        event.setEndTime(eventRequest.getEndTime());
        event.setLocation(eventRequest.getLocation());
        event.setOrganizer(eventRequest.getOrganizer());
        event.setStatus(EventStatus.UNPUBLISHED);
        // set other fields
        eventRepository.save(event);
    }

    public void updateEvent(EventRequest eventRequest, String id) {
        Event event = eventRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setStartDate(eventRequest.getStartDate());
        event.setEndDate(eventRequest.getEndDate());
        event.setStartTime(eventRequest.getStartTime());
        event.setEndTime(eventRequest.getEndTime());
        event.setLocation(eventRequest.getLocation());
        event.setOrganizer(eventRequest.getOrganizer());
        eventRepository.save(event);
    }

    public void publishEvents(List<String> eventIds) {
        eventIds.forEach(id -> {
            Event event = eventRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            event.setStatus(EventStatus.PUBLISHED);
            kafkaTemplate.send("promoTopic", new PromoEvent(event.getId(), event.getName(), event.getDescription(),
                            event.getStartDate(), event.getEndDate(), event.getStartTime(),
                            event.getEndTime(), event.getLocation(), event.getOrganizer()));
            log.info("Published event - {}", event.getName());
            eventRepository.save(event);
        });
    }



    EventResponse mapToDto(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .location(event.getLocation())
                .organizer(event.getOrganizer())
                .build();
    }
}

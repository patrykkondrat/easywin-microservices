package com.easywin.promoservice.service;

import com.easywin.promoservice.dto.EventResponse;
import com.easywin.promoservice.event.PromoEvent;
import com.easywin.promoservice.model.Event;
import com.easywin.promoservice.model.EventStatus;
import com.easywin.promoservice.repository.EventRepository;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private KafkaTemplate<String, PromoEvent> kafkaTemplate;

    @InjectMocks
    private EventService eventService;

    @Test
    void getAllEvents() {
        Event event1 = this.generateEvent("1");
        Event event2 = this.generateEvent("2");

        List<Event> events = List.of(event1, event2);

        when(eventRepository.findAll()).thenReturn(events);

        List<EventResponse> result = eventService.getAllEvents();

        assertEquals(2, result.size());
        assertEquals("Test1", result.get(0).getName());
        assertEquals("Test2", result.get(1).getName());
        assertEquals("Test1", result.get(0).getDescription());
        assertEquals("Test2", result.get(1).getDescription());
        assertEquals("Test1", result.get(0).getStartDate());
        assertEquals("Test2", result.get(1).getStartDate());
    }

    @Test
    void getEvent() {
        Event event = this.generateEvent("1");

        when(eventRepository.findById("1")).thenReturn(java.util.Optional.of(event));

        EventResponse result = eventService.getEvent("1");

        assertEquals("Test1", result.getName());
        assertEquals("Test1", result.getDescription());
        assertEquals("Test1", result.getStartDate());
    }

    @Test
    void deleteEvent_CorrectId() {
        String id = "1";

        eventService.deleteEvent(id);

        verify(eventRepository).deleteById(id);
    }

    @Test
    void deleteEvent_IncorrectId() {
        String id = "1";

        doThrow(new IllegalArgumentException()).when(eventRepository).deleteById(id);

        assertThrows(IllegalArgumentException.class, () -> eventService.deleteEvent(id));
    }

    @Test
    void createEvent() {
    }

    @Test
    void updateEvent() {
    }

    @Test
    void publishEvents() {
    }

    @Test
    void mapToDto() {
    }

    private Event generateEvent(String suffix) {
        Event event = new Event();
        event.setId(suffix);
        event.setName("Test" + suffix);
        event.setDescription("Test" + suffix);
        event.setStartDate("Test" + suffix);
        event.setEndDate("Test" + suffix);
        event.setStartTime("Test" + suffix);
        event.setEndTime("Test" + suffix);
        event.setLocation("Test" + suffix);
        event.setOrganizer("Test" + suffix);
        event.setOrganizerEmail("Test" + suffix);
        event.setStatus(EventStatus.UNPUBLISHED);
        return event;
    }
}
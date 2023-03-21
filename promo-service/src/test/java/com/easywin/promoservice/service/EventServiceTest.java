package com.easywin.promoservice.service;

import com.easywin.promoservice.dto.EventRequest;
import com.easywin.promoservice.dto.EventResponse;
import com.easywin.promoservice.event.PromoEvent;
import com.easywin.promoservice.model.Event;
import com.easywin.promoservice.model.EventStatus;
import com.easywin.promoservice.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
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
        EventRequest eventRequest = new EventRequest("Test1", "Test1",
                "Test1", "Test1", "Test1", "Test1", "Test1","Test1");

        Event event = this.generateEvent("1");

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        eventService.createEvent(eventRequest);

        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void updateEvent() {
        Event event = this.generateEvent("2");

        EventRequest eventRequest = new EventRequest();
        eventRequest.setName("Test2");
        eventRequest.setDescription("Test2");
        eventRequest.setStartDate("Test2");
        eventRequest.setEndDate("ThisWillChange");
        eventRequest.setStartTime("Test2");
        eventRequest.setEndTime("ThisWillChangeToo");
        eventRequest.setLocation("Test2");
        eventRequest.setOrganizer("Test2");

        when(eventRepository.findById("2")).thenReturn(java.util.Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        eventService.updateEvent(eventRequest, "2");

        verify(eventRepository, times(1)).save(any(Event.class));

    }

    @Test
    void publishEvents() {
        Event event1 = this.generateEvent("4");
        Event event2 = this.generateEvent("5");
        List<String> ids = List.of("4", "5");

        when(eventRepository.findById("4")).thenReturn(Optional.of(event1));
        when(eventRepository.findById("5")).thenReturn(Optional.of(event2));

        eventService.publishEvents(ids);

        verify(kafkaTemplate, times(2)).send(anyString(), any(PromoEvent.class));
        verify(eventRepository, times(2)).save(any(Event.class));
        assertEquals(EventStatus.PUBLISHED, event1.getStatus());
        assertEquals(EventStatus.PUBLISHED, event2.getStatus());
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
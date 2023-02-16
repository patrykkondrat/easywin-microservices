package com.easywin.promoservice.controller;

import com.easywin.promoservice.dto.EventRequest;
import com.easywin.promoservice.dto.EventResponse;
import com.easywin.promoservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/id")
    public EventResponse getEvent(@RequestParam String id) {
        return eventService.getEvent(id);
    }

    @PostMapping("/create")
    public void createEvent(@RequestBody EventRequest eventRequest) {
        eventService.createEvent(eventRequest);
    }

    @PostMapping("/publish")
    public void publishEvents(@RequestParam List<String> eventId) {
        eventService.publishEvents(eventId);
    }

    @PutMapping("/update")
    public void updateEvent(@RequestBody EventRequest eventRequest, @RequestParam String id) {
        eventService.updateEvent(eventRequest, id);
    }

    @DeleteMapping("/delete")
    public void deleteEvent(@RequestParam String id) {
        eventService.deleteEvent(id);
    }
}

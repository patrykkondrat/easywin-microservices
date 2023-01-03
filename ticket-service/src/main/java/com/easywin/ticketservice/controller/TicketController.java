package com.easywin.ticketservice.controller;

import com.easywin.ticketservice.dto.TicketRequest;
import com.easywin.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeTicket(@RequestBody TicketRequest ticketRequest) {
        ticketService.placeTicket(ticketRequest);
        return "Ticket accepted";
    }
}

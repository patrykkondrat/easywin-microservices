package com.easywin.ticketservice.controller;

import com.easywin.ticketservice.dto.TicketRequest;
import com.easywin.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeTicket(TicketRequest ticketRequest) {
        ticketService.placeTicket(ticketRequest);
        return "Ticket accepted";
    }
}

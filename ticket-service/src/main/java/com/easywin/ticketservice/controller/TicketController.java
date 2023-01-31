package com.easywin.ticketservice.controller;

import com.easywin.ticketservice.dto.TicketRequest;
import com.easywin.ticketservice.service.TicketService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "bet", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "bet")
    @Retry(name = "bet")
    public CompletableFuture<String> placeTicket(@RequestBody TicketRequest ticketRequest) {
        log.info("placeTicket called with {}", ticketRequest);
        return CompletableFuture.supplyAsync(() -> ticketService.placeTicket(ticketRequest));
    }

    public CompletableFuture<String> fallbackMethod(TicketRequest ticketRequest, RuntimeException runtimeException) {
        log.info("fallbackMethod called with {}", ticketRequest);
        return CompletableFuture.supplyAsync(() -> "Try again later.");
    }
}

package com.easywin.ticketservice.service;

import com.easywin.ticketservice.dto.BetToTicketResponse;
import com.easywin.ticketservice.dto.TicketLineItemsDto;
import com.easywin.ticketservice.dto.TicketRequest;
import com.easywin.ticketservice.model.Ticket;
import com.easywin.ticketservice.model.TicketLineItems;
import com.easywin.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final WebClient webClient;

    public void placeTicket(TicketRequest ticketRequest) {
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(UUID.randomUUID().toString());

        List<TicketLineItems> ticketLineItems = ticketRequest.getTicketLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        ticket.setTicketLineItemsList(ticketLineItems);

        List<String> betsId = ticket.getTicketLineItemsList().stream()
                .map(TicketLineItems::getBetId)
                .toList();

        BetToTicketResponse[] responsesArray =
                webClient.get()
                .uri("http://localhost:8080/api/bet/isbet",
                        uriBuilder ->
                                uriBuilder.queryParam("betId", betsId)
                                        .build())
                .retrieve()
                .bodyToMono(BetToTicketResponse[].class)
                .block();

        boolean responseArrays = Arrays.stream(responsesArray)
                .anyMatch(betToTicketResponse ->
                        betToTicketResponse.get_id() == null);

        System.out.println(betsId);
        System.out.println(responsesArray);
        System.out.println(responseArrays);

        if (responseArrays) {
            ticketRepository.save(ticket);
        } else {
            throw new IllegalArgumentException("Bet isn't available.");
        }
    }

    private TicketLineItems mapToDto(TicketLineItemsDto ticketLineItemsDto) {
        TicketLineItems ticketLineItems = new TicketLineItems();
        ticketLineItems.setBetId(ticketLineItemsDto.getBetId());
        ticketLineItems.setChoice(ticketLineItemsDto.getChoice());
        ticketLineItems.setRate(ticketLineItemsDto.getRate());
        return ticketLineItems;
    }
}

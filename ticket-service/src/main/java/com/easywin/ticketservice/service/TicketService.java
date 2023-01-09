package com.easywin.ticketservice.service;

import com.easywin.ticketservice.dto.BetToTicketResponse;
import com.easywin.ticketservice.dto.TicketLineItemsDto;
import com.easywin.ticketservice.dto.TicketRequest;
import com.easywin.ticketservice.model.Ticket;
import com.easywin.ticketservice.model.TicketLineItems;
import com.easywin.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final WebClient.Builder webClientBuilder;

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

        Set<String> idsWithoutDuplicates = new HashSet<>(betsId);

        if (betsId.size() != idsWithoutDuplicates.size()) {
            throw new IllegalArgumentException("Duplicates in ticket.");
        }

        BetToTicketResponse[] responseArray =
                webClientBuilder.build().get()
                .uri("http://bet-service/api/bet/isbet",
                        uriBuilder ->
                            uriBuilder.queryParam("_id", betsId)
                                    .build())
                .retrieve()
                .bodyToMono(BetToTicketResponse[].class)
                .block();

        List<BetToTicketResponse> withoutNullResponseList= new ArrayList<>();
        for (BetToTicketResponse betToTicketResponse: responseArray) {
            if (betToTicketResponse != null) {
                withoutNullResponseList.add(betToTicketResponse);
            }
        }

        Double overall = 1.00;
        for (TicketLineItems response: ticketLineItems) {
            overall *= response.getRate();
        }
        ticket.setOverall(overall);


        if (responseArray != null && withoutNullResponseList.size() == idsWithoutDuplicates.size()) {
            log.info("Ticket number: " + ticket.getTicketNumber());
            ticketRepository.save(ticket);
        } else {
            throw new IllegalArgumentException("Bet isn't available");
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

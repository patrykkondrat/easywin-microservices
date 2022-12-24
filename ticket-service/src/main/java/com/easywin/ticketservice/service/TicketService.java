package com.easywin.ticketservice.service;

import com.easywin.ticketservice.dto.TicketLineItemsDto;
import com.easywin.ticketservice.dto.TicketRequest;
import com.easywin.ticketservice.model.Ticket;
import com.easywin.ticketservice.model.TicketLineItems;
import com.easywin.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public void placeTicket(TicketRequest ticketRequest) {
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(UUID.randomUUID().toString());

        List<TicketLineItems> ticketLineItems = ticketRequest.getTicketLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        ticket.setTicketLineItemsList(ticketLineItems);
        ticketRepository.save(ticket);

    }

    private TicketLineItems mapToDto(TicketLineItemsDto ticketLineItemsDto) {
        TicketLineItems ticketLineItems = new TicketLineItems();
        ticketLineItems.setRate(ticketLineItemsDto.getRate());
        ticketLineItems.setChoice(ticketLineItemsDto.getChoice());
        ticketLineItems.setBetId(ticketLineItems.getBetId());
        return ticketLineItems;
    }
}

package com.easywin.ticketservice.service;

import brave.Span;
import brave.Tracer;
import com.easywin.ticketservice.dto.BetToTicketResponse;
import com.easywin.ticketservice.dto.TicketLineItemsDto;
import com.easywin.ticketservice.dto.TicketRequest;
import com.easywin.ticketservice.dto.WalletDecrease;
import com.easywin.ticketservice.event.TicketPlaceEvent;
import com.easywin.ticketservice.model.Ticket;
import com.easywin.ticketservice.model.TicketLineItems;
import com.easywin.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final Tracer tracer;
    private final KafkaTemplate<String, TicketPlaceEvent> kafkaTemplate;

    @Transactional
    public String placeTicket(TicketRequest ticketRequest) {
        Ticket ticket = new Ticket();
        Double tax = 0.12;
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

        Span betServiceLookup = tracer.nextSpan().name("BetServiceLookup");

        try (Tracer.SpanInScope inScope = tracer.withSpanInScope(betServiceLookup.start())) {

            betServiceLookup.tag("call", "bet-service");

            BetToTicketResponse[] responseArray =
                    webClientBuilder.build().get()
                            .uri("http://bet-service/api/bet/isbet",
                                    uriBuilder ->
                                            uriBuilder.queryParam("_id", betsId)
                                                    .build())
                            .retrieve()
                            .bodyToMono(BetToTicketResponse[].class)
                            .block();

            List<BetToTicketResponse> withoutNullResponseList = new ArrayList<>();
            assert responseArray != null;
            for (BetToTicketResponse betToTicketResponse : responseArray) {
                if (betToTicketResponse != null) {
                    withoutNullResponseList.add(betToTicketResponse);
                }
            }

            List<BetToTicketResponse> sortedResponseFromBetService = withoutNullResponseList.stream().sorted(Comparator.comparing(BetToTicketResponse::get_id)).toList();
            List<TicketLineItems> sortedTicketRequest = ticketLineItems.stream().sorted(Comparator.comparing(TicketLineItems::getBetId)).toList();

            for (int i = 0; i < withoutNullResponseList.size(); i++) {
                if (!isValidBetRequest(sortedTicketRequest.get(i), sortedResponseFromBetService.get(i))) {
                    throw new IllegalArgumentException("Invalid request.");
                }
            }

            Double overall = 1.00;
            for (TicketLineItems response: ticketLineItems) {
                overall *= response.getRate();
            }


            ticket.setOverall(overall);
            ticket.setTotalStake(ticketRequest.getTotalStake());
            ticket.setTotalWin(overall * ticketRequest.getTotalStake() * (1 - tax));


            if (withoutNullResponseList.size() == idsWithoutDuplicates.size()) {
                WalletDecrease walletDecrease = new WalletDecrease(ticketRequest.getWalletId(), ticketRequest.getTotalStake());
                webClientBuilder.build().post()
                        .uri("http://wallet-service/api/wallet",
                                uriBuilder ->
                                        uriBuilder.queryParam("id", walletDecrease.getId())
                                                .queryParam("value",-walletDecrease.getDecreaseValue())
                                                .build())
                        .bodyValue(walletDecrease)
                        .retrieve()
                        .bodyToMono(WalletDecrease.class)
                        .block();


                ticketRepository.save(ticket);
                kafkaTemplate.send("ticketTopic",
                        new TicketPlaceEvent(ticket.getTicketNumber(), ticket.getTicketLineItemsList(), ticket.getTotalStake(), ticket.getTotalWin()));
                log.info("send - {}", ticket.getTicketNumber());
                return "Ticket accepted.";
            } else {
                throw new IllegalArgumentException("Bet isn't available");
            }
        } finally {
            betServiceLookup.flush();
        }
    }


    private boolean isValidBetRequest(TicketLineItems ticketLineItems, BetToTicketResponse response) {
        if (ticketLineItems.getBetId().equals(response.get_id())) {
            if (ticketLineItems.getChoice().equals(response.getHostname())
                    && ticketLineItems.getRate().toString().equals(response.getHostRate())) {
                return true;
            } else if (ticketLineItems.getChoice().equals(response.getGuestname())
                    && ticketLineItems.getRate().toString().equals(response.getGuestRate())) {
                return true;
            }
        }
        return false;
    }


    private TicketLineItems mapToDto(TicketLineItemsDto ticketLineItemsDto) {
        TicketLineItems ticketLineItems = new TicketLineItems();
        ticketLineItems.setBetId(ticketLineItemsDto.getBetId());
        ticketLineItems.setChoice(ticketLineItemsDto.getChoice());
        ticketLineItems.setRate(ticketLineItemsDto.getRate());
        return ticketLineItems;
    }
}

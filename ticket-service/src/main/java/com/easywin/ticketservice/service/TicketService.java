package com.easywin.ticketservice.service;

import brave.Span;
import brave.Tracer;
import com.easywin.ticketservice.dto.BetToTicketResponse;
import com.easywin.ticketservice.dto.TicketLineItemsDto;
import com.easywin.ticketservice.dto.TicketRequest;
import com.easywin.ticketservice.dto.WalletDecrease;
import com.easywin.ticketservice.event.TicketPlaceEvent;
import com.easywin.ticketservice.event.UpdateBetStatusInTicket;
import com.easywin.ticketservice.model.BetStatus;
import com.easywin.ticketservice.model.BillingStatus;
import com.easywin.ticketservice.model.Ticket;
import com.easywin.ticketservice.model.TicketLineItems;
import com.easywin.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

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
        double tax = 0.12;
        ticket.setTicketNumber(UUID.randomUUID().toString());

        List<TicketLineItems> ticketLineItems =  mapToDtoList(ticketRequest);
        ticket.setTicketLineItemsList(ticketLineItems);

        Set<String> betsId = checkDuplicates(ticket);

        Span betServiceLookup = tracer.nextSpan().name("BetServiceLookup");

        try (Tracer.SpanInScope inScope = tracer.withSpanInScope(betServiceLookup.start())) {

            betServiceLookup.tag("call", "bet-service");

            BetToTicketResponse[] responseArray = getBetsFromBetService(betsId);


            List<BetToTicketResponse> withoutNullResponseList = new ArrayList<>();
            assert responseArray != null;
            for (BetToTicketResponse betToTicketResponse : responseArray) {
                if (betToTicketResponse != null && betToTicketResponse.getBetStatus() == BetStatus.PENDING) {
                    withoutNullResponseList.add(betToTicketResponse);
                }
            }

            List<BetToTicketResponse> sortedResponseFromBetService =
                    withoutNullResponseList.stream()
                            .sorted(Comparator.comparing(BetToTicketResponse::get_id))
                            .toList();
            List<TicketLineItems> sortedTicketRequest =
                    ticketLineItems.stream()
                            .sorted(Comparator.comparing(TicketLineItems::getBetId))
                            .toList();


            for (int i = 0; i < withoutNullResponseList.size(); i++) {
                if (!isValidBetRequest(sortedTicketRequest.get(i), sortedResponseFromBetService.get(i))) {
                    throw new IllegalArgumentException("Bad bet in request.");
                }
            }

            double overallExchange = calculateOverallExchange(ticketLineItems);
            ticket.setOverall(overallExchange);
            ticket.setTotalStake(ticketRequest.getTotalStake());
            ticket.setTotalWin(overallExchange * ticketRequest.getTotalStake() * (1 - tax));
            ticket.setBillingStatus(BillingStatus.PENDING);


            if (withoutNullResponseList.size() == betsId.size()) {
                walletReduction(ticketRequest);

                ticketRepository.save(ticket);
                kafkaTemplate.send("ticketTopic",
                        new TicketPlaceEvent(ticket.getTicketNumber(),
                                ticket.getTicketLineItemsList(),
                                ticket.getTotalStake(), ticket.getTotalWin()));
                return "Ticket accepted.";
            } else {
                throw new IllegalArgumentException("Invalid Ticket");
            }
        } finally {
            betServiceLookup.flush();
        }
    }

    private double calculateOverallExchange(List<TicketLineItems> ticketLineItems) {
        double overallExchange = 1;
        for (TicketLineItems response : ticketLineItems) {
            overallExchange *= response.getRate();
        }
        return overallExchange;
    }

    private void walletReduction(TicketRequest ticketRequest) {
        WalletDecrease walletDecrease = new WalletDecrease(ticketRequest.getWalletId(), ticketRequest.getTotalStake());
        webClientBuilder.build().post()
                .uri("http://wallet-service/api/wallet",
                        uriBuilder ->
                                uriBuilder.queryParam("id", walletDecrease.getId())
                                        .queryParam("value", -walletDecrease.getDecreaseValue())
                                        .build())
                .bodyValue(walletDecrease)
                .retrieve()
                .bodyToMono(WalletDecrease.class)
                .block();
    }

    private BetToTicketResponse[] getBetsFromBetService(Set<String> betsId) {
        return webClientBuilder.build().get()
                .uri("http://bet-service/api/bet/isbet",
                        uriBuilder ->
                                uriBuilder.queryParam("_id", betsId)
                                        .build())
                .retrieve()
                .bodyToMono(BetToTicketResponse[].class)
                .block();
    }

    private Set<String> checkDuplicates(Ticket ticket) {
        Set<String> betsId =
                ticket.getTicketLineItemsList().stream()
                        .map(TicketLineItems::getBetId).collect(Collectors.toSet());

        if (betsId.size() == ticket.getTicketLineItemsList().size()) {
            return betsId;
        } else {
            throw new IllegalArgumentException("Duplicates in ticket " + ticket.getTicketNumber());
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

    private List<TicketLineItems> mapToDtoList(TicketRequest ticketRequest) {
        return ticketRequest.getTicketLineItemsDtoList().stream()
                .map(this::mapToDto).toList();
    }

    private TicketLineItems mapToDto(TicketLineItemsDto ticketLineItemsDto) {
        TicketLineItems ticketLineItems = new TicketLineItems();
        ticketLineItems.setBetId(ticketLineItemsDto.getBetId());
        ticketLineItems.setChoice(ticketLineItemsDto.getChoice());
        ticketLineItems.setRate(ticketLineItemsDto.getRate());
        ticketLineItems.setBetStatus(BetStatus.PENDING);
        return ticketLineItems;
    }

    @KafkaListener(topics = "betTopic")
    public void updateTickets(UpdateBetStatusInTicket status) {
        List<Ticket> tickets = ticketRepository.findAllByTicketLineItemsList_BetIdAndBillingStatus(status.getId(), BillingStatus.PENDING);
        for (Ticket ticket: tickets) {
            boolean allBetsInTicketLineItemsResloved = true;
            List<TicketLineItems> ticketLineItemsList = ticket.getTicketLineItemsList();
            for (TicketLineItems bets: ticketLineItemsList) {
                if (bets.getBetId().equals(status.getId())) {
                    bets.setBetStatus(status.getBetStatus());
                }
                if (bets.getBetStatus().equals(BetStatus.PENDING)){
                    allBetsInTicketLineItemsResloved = false;
                }
            }
            if (allBetsInTicketLineItemsResloved){
                ticket.setBillingStatus(BillingStatus.TO_PAY);
            }
            ticketRepository.save(ticket);
        }
    }
}

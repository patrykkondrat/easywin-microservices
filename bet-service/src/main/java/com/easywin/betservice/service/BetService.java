package com.easywin.betservice.service;

import com.easywin.betservice.dto.BetRequest;
import com.easywin.betservice.dto.BetResponse;
import com.easywin.betservice.dto.BetToTicketResponse;
import com.easywin.betservice.event.UpdateBetStatusInTicket;
import com.easywin.betservice.model.Bet;
import com.easywin.betservice.model.BetStatus;
import com.easywin.betservice.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetService {

    private final BetRepository betRepository;

    private final KafkaTemplate<String, UpdateBetStatusInTicket> kafkaTemplate;

    public void createBet(BetRequest betRequest) {
        Bet bet = Bet.builder()
                ._id(UUID.randomUUID().toString())
                .description(betRequest.getDescription())
                .hostRate(betRequest.getHostRate())
                .guestRate(betRequest.getGuestRate())
                .guestname(betRequest.getGuestname())
                .hostname(betRequest.getHostname())
                .date(betRequest.getDate())
                .discipline(betRequest.getDiscipline())
                .betStatus(BetStatus.PENDING)
                .build();
        log.info("Bet " + bet.toString() + " saved");
        betRepository.save(bet);

    }

    public void deleteBet(String _id) {
        if (!betRepository.existsById(_id)) {
            throw new RuntimeException("Bet with id " + _id + " not found");
        }
        betRepository.deleteById(_id);
        log.info("Bet " + _id + " deleted");
    }

    public List<BetResponse> getAllBet() {
        List<Bet> bets = betRepository.findAll();
        return bets.stream().map(this::mapToBetResponse).toList();
    }

    public BetResponse getBetById(String id) {
        return betRepository.findById(id).map(this::mapToBetResponse).orElse(null);
    }

    private BetResponse mapToBetResponse(Bet bet) {
        return BetResponse.builder()
                ._id(bet.get_id())
                .description(bet.getDescription())
                .hostRate(bet.getHostRate())
                .guestRate(bet.getGuestRate())
                .guestname(bet.getGuestname())
                .hostname(bet.getHostname())
                .date(bet.getDate())
                .discipline(bet.getDiscipline())
                .betStatus(bet.getBetStatus())
                .build();
    }



    @Transactional(readOnly = true)
    public List<BetToTicketResponse> isBetInBets(List<String> _id) {
        Set<String> idSet = new HashSet<>(_id);
        List<Bet> listOfBets = idSet.stream().map(betRepository::findById)
                .filter(Optional::isPresent).map(Optional::get).toList();
        return listOfBets.stream()
                .map(bet -> BetToTicketResponse.builder()
                        ._id(bet.get_id())
                        .hostRate(bet.getHostRate())
                        .guestRate(bet.getGuestRate())
                        .hostname(bet.getHostname())
                        .guestname(bet.getGuestname())
                        .betStatus(bet.getBetStatus())
                        .build())
                .collect(Collectors.toList());
    }

    public Set<String> getDisciplines() {
        // TODO change func in Repository to select unique values from Mongo
        List<Bet> bets = betRepository.findAll();
        return betRepository.findAll().stream().map(Bet::getDiscipline).collect(Collectors.toSet());
    }

    public List<BetResponse> getBetByDiscipline(String discipline) {
//        if (this.getDisciplines().contains(discipline)) {
//            return null;
//        }
        return betRepository.findBetByDiscipline(discipline).stream().map(this::mapToBetResponse).toList();
    }


    public void changeBetStatus(String id, BetStatus betStatus) {
        Bet bet = betRepository.findById(id).orElseThrow(() -> new RuntimeException("Bet with id " + id + " not found"));
        bet.setBetStatus(betStatus);
        betRepository.save(bet);
        kafkaTemplate.send("betTopic", new UpdateBetStatusInTicket(bet.get_id(), bet.getBetStatus()));
    }
}

package com.easywin.betservice.service;

import com.easywin.betservice.dto.BetRequest;
import com.easywin.betservice.dto.BetResponse;
import com.easywin.betservice.dto.BetToTicketResponse;
import com.easywin.betservice.model.Bet;
import com.easywin.betservice.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetService {

    private final BetRepository betRepository;

    public void createBet(BetRequest betRequest){
        Bet bet = Bet.builder()
                ._id(UUID.randomUUID().toString())
                .description(betRequest.getDescription())
                .hostRate(betRequest.getHostRate())
                .guestRate(betRequest.getGuestRate())
                .guestname(betRequest.getGuestname())
                .hostname(betRequest.getHostname())
                .date(betRequest.getDate())
                .discipline(betRequest.getDiscipline())
                .build();
        log.info("Bet " + bet.toString() + " saved");
        betRepository.save(bet);

    }

    public void deleteBet(String _id){
        betRepository.deleteById(_id);
        log.info("Bet " + _id + " deleted");
    }

    public List<BetResponse> getAllBet() {
        List<Bet> bets = betRepository.findAll();
        return bets.stream().map(this::mapToBetResponse).toList();
    }

    public Optional<BetResponse> getBetById(String id) {
        return betRepository.findById(id).map(this::mapToBetResponse);
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
                .build();
    }

    public Set<String> getDisciplines() {
        List<Bet> bets = betRepository.findAll();
        return bets.stream().map(Bet::getDiscipline).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public List<BetToTicketResponse> isBetInBets(List<String> _id) {
        Set<String> idSet = new HashSet<>(_id);
        List<Optional<Bet>> listOfBets = idSet.stream().map(betRepository::findById).toList();
        return listOfBets.stream()
                .map(bet -> {
                    if (bet.isPresent()) {
                        return BetToTicketResponse.builder()
                                ._id(bet.orElseThrow().get_id())
                                .hostRate(bet.orElseThrow().getHostRate())
                                .guestRate(bet.orElseThrow().getGuestRate())
                                .hostname(bet.orElseThrow().getHostname())
                                .guestname(bet.orElseThrow().getGuestname())
                                .build();
                    } else {
                        return null;
                    }
                })
                .toList();
    }

    public List<BetResponse> getBetByDiscipline(String discipline) {
        if (this.getDisciplines().contains(discipline)) {
            return null;
        }
        List<Bet> bets = betRepository.findAllByDiscipline(discipline);
        return bets.stream().map(this::mapToBetResponse).toList();
    }
}

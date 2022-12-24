package com.easywin.betservice.service;

import com.easywin.betservice.dto.BetRequest;
import com.easywin.betservice.dto.BetResponse;
import com.easywin.betservice.model.Bet;
import com.easywin.betservice.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetService {

    private final BetRepository betRepository;

    // create
    public void createBet(BetRequest betRequest){
        Bet bet = Bet.builder()
                .description(betRequest.getDescription())
                .hostRate(betRequest.getHostRate())
                .guestRate(betRequest.getGuestRate())
                .guestname(betRequest.getGuestname())
                .hostname(betRequest.getHostname())
                .date(betRequest.getDate())
                .discipline(betRequest.getDiscipline())
                .build();
        betRepository.save(bet);
        log.info("Bet " + bet.get_id() + " saved");
    }
    // delete
    public void deleteBet(String id){
        betRepository.deleteById(id);
        log.info("Bet " + id + " delated");
    }
    // get
    public List<BetResponse> getAllBet() {
        List<Bet> bets = betRepository.findAll();
        return bets.stream().map(this::mapToBetResponse).toList();
    }
    // get by id
    public Optional<BetResponse> getBetById(String id) {
        return betRepository.findById(id).map(this::mapToBetResponse);
    }

    private BetResponse mapToBetResponse(Bet bet) {
        return BetResponse.builder()
                .id(bet.get_id())
                .description(bet.getDescription())
                .hostRate(bet.getHostRate())
                .guestRate(bet.getGuestRate())
                .guestname(bet.getGuestname())
                .hostname(bet.getHostname())
                .date(bet.getDate())
                .discipline(bet.getDiscipline())
                .build();
    }
}

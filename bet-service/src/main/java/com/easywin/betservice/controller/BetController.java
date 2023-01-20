package com.easywin.betservice.controller;

import com.easywin.betservice.dto.BetRequest;
import com.easywin.betservice.dto.BetResponse;
import com.easywin.betservice.dto.BetToTicketResponse;
import com.easywin.betservice.service.BetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bet")
@RequiredArgsConstructor
@Slf4j
public class BetController {

    private final BetService betService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BetResponse>  getAllBets(){
        return betService.getAllBet();
    }

    @GetMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    public Optional<BetResponse> getBetById(@RequestParam("Id") String Id) {
        return betService.getBetById(Id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBet(@RequestBody BetRequest betRequest){
        betService.createBet(betRequest);
    }

    @DeleteMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBet(@RequestParam("Id") String Id){
        betService.deleteBet(Id);
    }

    @GetMapping("/isbet")
    @ResponseStatus(HttpStatus.OK)
    public List<BetToTicketResponse> isBet(@RequestParam List<String> _id) {
        return betService.isBetInBets(_id);
    }
}

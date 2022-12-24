package com.easywin.betservice.controller;

import com.easywin.betservice.dto.BetRequest;
import com.easywin.betservice.dto.BetResponse;
import com.easywin.betservice.service.BetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bet")
@RequiredArgsConstructor
public class BetController {
    @Autowired
    private final BetService betService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BetResponse>  getAllBets(){
        return betService.getAllBet();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<BetResponse> getBetById(@PathVariable("id") String id) {
        return betService.getBetById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBet(@RequestBody BetRequest betRequest){
        betService.createBet(betRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBet(@PathVariable("id") String id){
        betService.deleteBet(id);
    }
}

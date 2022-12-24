package com.easywin.walletservice.controller;

import com.easywin.walletservice.dto.WalletRequest;
import com.easywin.walletservice.model.Wallet;
import com.easywin.walletservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<Wallet> getIdBallance(@RequestBody WalletRequest walletRequest) {
        return walletService.getBalanceById(walletRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Wallet createWallet() {
        return walletService.createWallet();

    }

    @PostMapping("/{value}")
    @ResponseStatus(HttpStatus.OK)
    public void increaseBalance(@RequestBody WalletRequest walletRequest, @PathVariable Double value) {
        String out = walletService.changeBalance(walletRequest, value);
        if (value >= 0) {
            log.info("Account " + walletRequest.get_id() + " increase.");
        } else {
            log.info("Account " + walletRequest.get_id() + " decrease.");
        }
    }
}

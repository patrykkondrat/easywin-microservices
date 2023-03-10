package com.easywin.walletservice.controller;

import com.easywin.walletservice.dto.WalletRequest;
import com.easywin.walletservice.model.Wallet;
import com.easywin.walletservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    public Wallet getIdBallance(@RequestBody WalletRequest walletRequest) {
        return walletService.getBalanceById(walletRequest);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Wallet createWallet() {
        return walletService.createWallet();

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void increaseBalance(@RequestParam("id") String id, @RequestParam("value") String value) {
        walletService.changeBalance(id, value);
        if (Double.parseDouble(value) >= 0) {
            log.info("Account " + id + " increase.");
        } else {
            log.info("Account " + id + " decrease.");
        }
    }
}

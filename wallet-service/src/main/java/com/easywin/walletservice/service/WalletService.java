package com.easywin.walletservice.service;

import com.easywin.walletservice.dto.WalletRequest;
import com.easywin.walletservice.model.Wallet;
import com.easywin.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet createWallet() {
        Wallet wallet = new Wallet();
        wallet.set_id(UUID.randomUUID().toString());
        wallet.setBalance(0.00);
        walletRepository.save(wallet);
        return wallet;
    }

    public Optional<Wallet> getBalanceById(WalletRequest walletRequest) {
        return walletRepository.findById(walletRequest.get_id());
    }

    public String changeBalance(WalletRequest walletRequest, double value) {
        Wallet wallet = (Wallet) walletRepository.findById(walletRequest.get_id()).stream().map(this::mapToWallet);
        if (wallet.getBalance() + value < 0.00) {
            return "Can't decrease over 0.00";
        }
        wallet.setBalance(wallet.getBalance() + value);
        walletRepository.save(wallet);
        return "Success";
    }

    private Wallet mapToWallet(Wallet wallet) {
        return Wallet.builder()
                ._id(wallet.get_id())
                .balance(wallet.getBalance())
                .build();
    }


}

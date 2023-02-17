package com.easywin.walletservice.service;

import com.easywin.walletservice.dto.WalletRequest;
import com.easywin.walletservice.model.Wallet;
import com.easywin.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Wallet getBalanceById(WalletRequest walletRequest) {
        return walletRepository.findById(walletRequest.get_id()).orElse(null);
    }

    public void changeBalance(String id, String value) {
        Wallet wallet = walletRepository.findById(id).map(this::mapToWallet).orElseThrow();
        if (wallet.getBalance() + Double.parseDouble(value) < 0.00) {
            throw new IllegalArgumentException("Can't decrease under 0.00");
        }
        wallet.setBalance(wallet.getBalance() + Double.parseDouble(value));
        walletRepository.save(wallet);
    }

    private Wallet mapToWallet(Wallet wallet) {
        return Wallet.builder()
                ._id(wallet.get_id())
                .balance(wallet.getBalance())
                .build();
    }


}

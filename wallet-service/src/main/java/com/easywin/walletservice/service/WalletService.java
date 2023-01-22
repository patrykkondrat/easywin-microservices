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

    public void changeBalance(WalletRequest walletRequest, String value) {
        Optional<Wallet> wallet = walletRepository.findById(walletRequest.get_id()).map(this::mapToWallet);
        if (wallet.get().getBalance() + Double.parseDouble(value) < 0.00) {
            throw new IllegalArgumentException("Can't decrease over 0.00");
        }
        wallet.get().setBalance(wallet.get().getBalance() + Double.parseDouble(value));
        walletRepository.save(wallet.get());
    }

    private Wallet mapToWallet(Wallet wallet) {
        return Wallet.builder()
                ._id(wallet.get_id())
                .balance(wallet.getBalance())
                .build();
    }


}

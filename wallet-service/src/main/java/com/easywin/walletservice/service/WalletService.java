package com.easywin.walletservice.service;

import com.easywin.walletservice.dto.WalletRequest;
import com.easywin.walletservice.model.Wallet;
import com.easywin.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public Optional<Wallet> getBalanceById(WalletRequest walletRequest) {
        return walletRepository.findById(walletRequest.getId());
    }

    public void changeBalance(WalletRequest walletRequest, double value) {
        Wallet wallet = (Wallet) walletRepository.findById(walletRequest.getId()).stream().map(this::mapToWallet);
        wallet.setBalance(wallet.getBalance() + value);
        walletRepository.save(wallet);
    }

    private Wallet mapToWallet(Wallet wallet) {
        return Wallet.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .build();
    }


}

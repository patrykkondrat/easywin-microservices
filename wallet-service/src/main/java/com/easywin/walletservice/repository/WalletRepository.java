package com.easywin.walletservice.repository;

import com.easywin.walletservice.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {

    Optional<Wallet> findById(String s);
}

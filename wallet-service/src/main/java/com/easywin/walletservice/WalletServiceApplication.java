package com.easywin.walletservice;

import com.easywin.walletservice.model.Wallet;
import com.easywin.walletservice.repository.WalletRepository;
import com.easywin.walletservice.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletServiceApplication {


	@Autowired
	private WalletService walletService;

	public static void main(String[] args) {
		SpringApplication.run(WalletServiceApplication.class, args);
	}

	CommandLineRunner run(String... args) {
		return args1 -> {
			walletService.createWallet();
			walletService.createWallet();
			walletService.createWallet();

		};
	}
}

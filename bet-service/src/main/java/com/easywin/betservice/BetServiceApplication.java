package com.easywin.betservice;

import com.easywin.betservice.model.Bet;
import com.easywin.betservice.repository.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BetServiceApplication {

	@Autowired
	private BetRepository betRepository;

	public static void main(String[] args) {
		SpringApplication.run(BetServiceApplication.class, args);
	}

	CommandLineRunner loadData() {
		return args -> {
			Bet bet = new Bet();
			bet.setDate("22-12-2022");
			bet.setDescription("TEST");
			bet.setDiscipline("TEST");
			bet.setHostname("TEST1");
			bet.setGuestname("TEST2");
			bet.setHostRate("2.00");
			bet.setGuestRate("2.00");

			betRepository.save(bet);

		};
	}

}

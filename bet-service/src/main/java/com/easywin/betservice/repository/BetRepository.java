package com.easywin.betservice.repository;

import com.easywin.betservice.model.Bet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BetRepository extends MongoRepository<Bet, String> {
}

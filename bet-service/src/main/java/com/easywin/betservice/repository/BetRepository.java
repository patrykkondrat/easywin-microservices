package com.easywin.betservice.repository;

import com.easywin.betservice.dto.BetToTicketResponse;
import com.easywin.betservice.model.Bet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BetRepository extends MongoRepository<Bet, String> {
    List<BetToTicketResponse> findBy_id(List<String> _id);
}

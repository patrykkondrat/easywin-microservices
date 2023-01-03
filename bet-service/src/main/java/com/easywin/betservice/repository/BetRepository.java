package com.easywin.betservice.repository;

import com.easywin.betservice.model.Bet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends MongoRepository<Bet, String> {
    List<Bet> findBy_id(List<String> _id);
}

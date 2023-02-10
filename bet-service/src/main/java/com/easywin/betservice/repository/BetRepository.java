package com.easywin.betservice.repository;

import com.easywin.betservice.model.Bet;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends MongoRepository<Bet, String> {

    @Aggregation("{ $group: { _id: '$discipline', uniqueValues: { $addToSet: '$discipline' } } }")
    List<Bet> findDistinctByDiscipline();
    List<Bet> findBetByDiscipline(String discipline);
}

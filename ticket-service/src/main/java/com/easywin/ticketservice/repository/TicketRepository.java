package com.easywin.ticketservice.repository;

import com.easywin.ticketservice.model.BillingStatus;
import com.easywin.ticketservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByTicketLineItemsList_BetIdAndBillingStatus(String betId, BillingStatus billingStatus);
}

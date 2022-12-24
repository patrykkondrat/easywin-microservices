package com.easywin.ticketservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "t_ticket_line")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long BetId;

    private String choice;

    private Float rate;
}

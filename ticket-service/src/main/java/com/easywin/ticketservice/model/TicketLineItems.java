package com.easywin.ticketservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "t_ticket_line_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _id;
    private String betId;
    private String choice;
    private Double rate;
}

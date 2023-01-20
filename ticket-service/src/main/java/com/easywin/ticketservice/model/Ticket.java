package com.easywin.ticketservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_ticket")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _id;
    private String ticketNumber;
    @OneToMany(cascade = CascadeType.ALL)
    private List<TicketLineItems> ticketLineItemsList;
    private Double overall;
    private Double totalStake;
    private Double totalWin;
}

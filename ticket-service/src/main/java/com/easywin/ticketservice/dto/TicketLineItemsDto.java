package com.easywin.ticketservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketLineItemsDto {
    private Long id;

    private Long BetId;

    private String choice;

    private Float rate;

}

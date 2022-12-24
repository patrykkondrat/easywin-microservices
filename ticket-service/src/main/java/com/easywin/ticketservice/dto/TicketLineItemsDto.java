package com.easywin.ticketservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketLineItemsDto {
    private Long id;

    private Long BetId;

    private String choice;

    private Float rate;

}

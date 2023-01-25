package com.easywin.ticketservice.dto;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TicketLineItemsDto {
    private Long _id;
    private String betId;
    private String choice;
    private Double rate;
}

package com.easywin.ticketservice.dto;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class BetToTicketResponse {
    private String _id;
    private String hostname;
    private String guestname;
    private String hostRate;
    private String guestRate;
}
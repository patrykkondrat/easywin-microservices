package com.easywin.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketPlaceEvent {
    private String ticketNumber;
    private Double totalStake;
    private Double totalWin;
}

package com.easywin.notificationservice.event;

import com.easywin.notificationservice.dto.TicketLineItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketPlaceEvent {
    private String ticketNumber;
    private List<TicketLineItems> TicketLineItemsList;
    private Double totalStake;
    private Double totalWin;
}

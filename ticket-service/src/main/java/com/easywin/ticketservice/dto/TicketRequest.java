package com.easywin.ticketservice.dto;

import com.easywin.ticketservice.model.TicketLineItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {
    private List<TicketLineItemsDto> ticketLineItemsDtoList;

}

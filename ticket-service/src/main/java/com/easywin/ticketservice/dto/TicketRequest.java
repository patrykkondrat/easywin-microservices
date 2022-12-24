package com.easywin.ticketservice.dto;

import com.easywin.ticketservice.model.TicketLineItems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {
    private List<TicketLineItemsDto> ticketLineItemsDtoList;

}

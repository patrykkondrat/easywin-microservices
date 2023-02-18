package com.easywin.betservice.dto;

import com.easywin.betservice.model.BetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BetToTicketResponse {
    private String _id;
    private String hostname;
    private String guestname;
    private String hostRate;
    private String guestRate;
    private BetStatus betStatus;
}

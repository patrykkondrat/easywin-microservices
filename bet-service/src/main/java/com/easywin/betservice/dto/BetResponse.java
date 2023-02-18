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
public class BetResponse {
    private String _id;
    private String description;
    private String hostRate;
    private String guestRate;
    private String hostname;
    private String guestname;
    private String date;
    private String discipline;
    private BetStatus betStatus;
}

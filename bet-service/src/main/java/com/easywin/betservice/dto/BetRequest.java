package com.easywin.betservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BetRequest {
    private String description;
    private String hostRate;
    private String guestRate;
    private String hostname;
    private String guestname;
    private String date;
    private String discipline;
}

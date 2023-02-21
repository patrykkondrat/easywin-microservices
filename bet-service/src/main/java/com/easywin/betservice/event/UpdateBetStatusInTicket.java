package com.easywin.betservice.event;

import com.easywin.betservice.model.BetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBetStatusInTicket {
    private String id;
    private BetStatus BetStatus;
}

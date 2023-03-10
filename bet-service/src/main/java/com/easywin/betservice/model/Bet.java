package com.easywin.betservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Document(value = "bet")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Bet {
    @Id
    private String _id;
    private String description;
    private String hostRate;
    private String guestRate;
    private String hostname;
    private String guestname;
    @Enumerated(EnumType.STRING)
    private BetStatus betStatus;
    private String date;
    private String discipline;

    // for example, winning by only 2 ways
}

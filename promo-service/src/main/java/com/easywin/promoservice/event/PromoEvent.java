package com.easywin.promoservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoEvent {
    private String id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String location;
    private String organizer;
    private String organizerEmail;
}

package com.easywin.promoservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EventResponse {
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

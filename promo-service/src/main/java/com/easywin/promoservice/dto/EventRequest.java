package com.easywin.promoservice.dto;


import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
public class EventRequest {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String location;
    private String organizer;
}

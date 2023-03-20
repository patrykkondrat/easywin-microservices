package com.easywin.promoservice.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "event")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    private String id;
    private String name;
    private String description;
    private String type;
    @Enumerated(EnumType.ORDINAL)
    private EventStatus status;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String location;
    private String organizer;
    private String organizerEmail;
}

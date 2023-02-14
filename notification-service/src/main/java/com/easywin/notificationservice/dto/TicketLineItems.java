package com.easywin.notificationservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TicketLineItems {
    private Long _id;
    private String betId;
    private String choice;
    private Double rate;
}

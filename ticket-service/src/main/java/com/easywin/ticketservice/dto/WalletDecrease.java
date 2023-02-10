package com.easywin.ticketservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class WalletDecrease {
    String Id;
    Double decreaseValue;
}

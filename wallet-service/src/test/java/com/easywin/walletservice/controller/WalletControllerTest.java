package com.easywin.walletservice.controller;

import com.easywin.walletservice.dto.WalletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getIdBallance() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/wallet/id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getWalletRequest())))
                .andExpect(status().isOk());
    }

    @Test
    void createWallet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/wallet/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isCreated());
    }

    @Test
    void increaseBalance() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "123");
        params.add("value", "100.00");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                        .andExpect(status().isOk());
    }

    private WalletRequest getWalletRequest() {
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.set_id("123");
        return walletRequest;
    }
}
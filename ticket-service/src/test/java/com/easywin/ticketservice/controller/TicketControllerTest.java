package com.easywin.ticketservice.controller;

import com.easywin.ticketservice.dto.TicketLineItemsDto;
import com.easywin.ticketservice.dto.TicketRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TicketControllerTest {

    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void placeTicket() throws Exception {
        TicketRequest ticketRequest = getTicketRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getTicketRequest())))
                .andExpect(status().isCreated());
    }


    private TicketRequest getTicketRequest() {
        List<TicketLineItemsDto> ticketLineItemsDtoList = new ArrayList<>();
        ticketLineItemsDtoList.add(new TicketLineItemsDto(1L, "213", "2", 12.00));
        ticketLineItemsDtoList.add(new TicketLineItemsDto(1L, "112", "2", 2.42));
        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setTicketLineItemsDtoList(ticketLineItemsDtoList);
        ticketRequest.setTotalStake(100.0);
        return ticketRequest;
    }
}
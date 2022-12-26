package com.easywin.betservice;

import com.easywin.betservice.dto.BetRequest;
import com.easywin.betservice.repository.BetRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class BetServiceApplicationTests {
	@Container
	static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private BetRepository betRepository;
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void getAllBets() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/bet"))
				.andExpect(status().isOk());
	}

	@Test
	void getBetById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/bet/id"))
				.andExpect(status().isOk());
	}

	@Test
	void createBet() throws Exception {
		BetRequest betRequest = getBetRequest();
		String betString = objectMapper.writeValueAsString(betRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/bet")
						.contentType(MediaType.APPLICATION_JSON)
						.content(betString))
						.andExpect(status().isCreated());
		assertEquals(betRepository.findAll().size(), 1);
	}

	@Test
	void deleteBet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/bet/id"))
				.andExpect(status().isOk());
		assertEquals(betRepository.findAll().size(), 0);
	}

	private BetRequest getBetRequest(){
		return BetRequest.builder()
				.description("Who win?")
				.hostRate("2.00")
				.guestRate("2.00")
				.hostname("Bayern")
				.guestname("Barca")
				.discipline("football")
				.date("22-12-2022")
				.build();
	}

}

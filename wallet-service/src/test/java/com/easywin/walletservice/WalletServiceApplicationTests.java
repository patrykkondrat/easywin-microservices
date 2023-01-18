package com.easywin.walletservice;

import com.easywin.walletservice.dto.WalletRequest;
import com.easywin.walletservice.repository.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WalletServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private WalletRepository walletRepository;



//	@Test
	void contextLoads() {
	}

//	@Test
	void getBalance() throws Exception {
		WalletRequest walletRequest = getWalletRequest();

		String requestBody = objectMapper.writeValueAsString(walletRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/wallet")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect((ResultMatcher) content().string("{\"id\":1,\"balance\":100.0}"));
	}

//	@Test
	void increaseBallance() throws Exception {
		String walletString = objectMapper.writeValueAsString(getWalletRequest());
		mockMvc.perform(MockMvcRequestBuilders.post("/api/wallet/-100.0")
						.param("_id", "1"))
				.andExpect(status().isOk());
	}

	private WalletRequest getWalletRequest() {
		return WalletRequest.builder()
				._id("1")
				.build();
	}

}

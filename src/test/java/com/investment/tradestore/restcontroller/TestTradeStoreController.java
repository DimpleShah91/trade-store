package com.investment.tradestore.restcontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.investment.tradestore.constants.ApplicationConstants;
import com.investment.tradestore.model.Trade;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestTradeStoreController {
	
	@Autowired
	private MockMvc mvc ;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Trade tradeExisting;
	private Trade newTrade;
	
	private static final String TRADE_SAVE_URI = "/trade/v1/save";
	private static final String CONTEXT_PATH = "/trade";

	
	@Before
	public void setUp() {
		tradeExisting =new Trade();
		tradeExisting.setTradeId("T2");
		tradeExisting.setTradeVersion(2);
		tradeExisting.setCounterParty("CP-2");
		tradeExisting.setBookId("B1");
		tradeExisting.setMaturityDate(LocalDate.of(2023, 12, 12));
		tradeExisting.setCreateDate(LocalDate.now());
		tradeExisting.setExpiryFlag('Y');
		
		newTrade =new Trade();
		newTrade.setTradeId("T5");
		newTrade.setTradeVersion(7);
		newTrade.setCounterParty("CP-7");
		newTrade.setBookId("B6");
		newTrade.setMaturityDate(LocalDate.of(2030, 12, 12));
		newTrade.setCreateDate(LocalDate.now());
		newTrade.setExpiryFlag('N');
		
	}
	
	@Test
	public void testTradeSaveSuccess() throws Exception {
		String json="{\"tradeId\":\"T5\",\"tradeVersion\":7,\"counterParty\":\"CP-7\",\"bookId\":\"B6\",\"maturityDate\":\"2027-04-04\",\"createDate\":\"2021-05-02\",\"expiryFlag\":\"N\"}";
		mvc.perform(MockMvcRequestBuilders.post(TRADE_SAVE_URI)
				.contextPath(CONTEXT_PATH)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testTradeSaveLowerVersion() throws Exception {
		newTrade.setTradeVersion(1);
		newTrade.setCounterParty("CP-1");
		mvc.perform(MockMvcRequestBuilders.post(TRADE_SAVE_URI)
				.contextPath(CONTEXT_PATH)
				.content(objectMapper.writeValueAsString(newTrade))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(result->assertEquals(ApplicationConstants.LOWER_VERSION_TRADE, result.getResolvedException().getMessage()));
	}
	
	@Test
	public void testTradeSaveSameVersion() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post(TRADE_SAVE_URI)
				.contextPath(CONTEXT_PATH)
				.content(objectMapper.writeValueAsString(newTrade))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
				
	}
	
	@Test
	public void testTradeSaveHigherVersion() throws Exception {
		newTrade.setTradeVersion(8);
		newTrade.setCounterParty("CP-8");
		mvc.perform(MockMvcRequestBuilders.post(TRADE_SAVE_URI)
				.contextPath(CONTEXT_PATH)
				.content(objectMapper.writeValueAsString(newTrade))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
				
	}
	
	
	@Test
	public void testTradeMaturityDatePast() throws Exception {
		newTrade.setMaturityDate(LocalDate.of(1991, 4, 4));
		mvc.perform(MockMvcRequestBuilders.post(TRADE_SAVE_URI)
				.contextPath(CONTEXT_PATH)
				.content(objectMapper.writeValueAsString(newTrade))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(result->assertEquals(ApplicationConstants.MATURITY_PAST_DATE, result.getResolvedException().getMessage()));
	}
	
	@Test
	public void testTradeMaturityDateSystem() throws Exception {
		newTrade.setMaturityDate(LocalDate.now());
		mvc.perform(MockMvcRequestBuilders.post(TRADE_SAVE_URI)
				.contextPath(CONTEXT_PATH)
				.content(objectMapper.writeValueAsString(newTrade))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
				
	}
	
	@Test
	public void testTradeMaturityDateFuture() throws Exception {
		newTrade.setMaturityDate(LocalDate.of(2027, 4, 4));
		mvc.perform(MockMvcRequestBuilders.post(TRADE_SAVE_URI)
				.contextPath(CONTEXT_PATH)
				.content(objectMapper.writeValueAsString(newTrade))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
				
	}
	


}

package com.investment.tradestore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.investment.tradestore.constants.ApplicationConstants;
import com.investment.tradestore.exception.LowerTradeVersionException;
import com.investment.tradestore.exception.MaturityDateException;
import com.investment.tradestore.exception.NoRecordFoundException;
import com.investment.tradestore.model.Trade;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class TestTradeStoreService {
	
	@Autowired
	private TradeStoreService tradeStoreService;
	
	private Trade tradeExisting;
	private Trade newTrade;

	@Before
	public void setUp() {
		tradeExisting =new Trade();
		tradeExisting.setTradeId("T3");
		tradeExisting.setTradeVersion(3);
		tradeExisting.setCounterParty("CP-3");
		tradeExisting.setBookId("B2");
		tradeExisting.setMaturityDate(LocalDate.of(2020, 12, 12));
		tradeExisting.setCreateDate(LocalDate.now());
		tradeExisting.setExpiryFlag('Y');
		
		newTrade =new Trade();
		newTrade.setTradeId("T5");
		newTrade.setTradeVersion(4);
		newTrade.setCounterParty("CP-4");
		newTrade.setBookId("B4");
		newTrade.setMaturityDate(LocalDate.of(2030, 12, 12));
		newTrade.setCreateDate(LocalDate.now());
		newTrade.setExpiryFlag('N');
		
	}
	

	@Test
	@Order(1)
	public void testGetTradeDetailsNotPresent() {
		Trade trade =new Trade();
		trade.setTradeId("T15");
		trade.setTradeVersion(4);
		trade.setCounterParty("CP-4");
		trade.setBookId("B4");
		trade.setMaturityDate(LocalDate.of(2030, 12, 12));
		trade.setCreateDate(LocalDate.now());
		trade.setExpiryFlag('N');
		Exception exception =assertThrows(NoRecordFoundException.class, ()->{
			tradeStoreService.testGetTradeDetails(trade);
		});
		assertEquals(ApplicationConstants.NO_RECORD_FOUND, exception.getMessage());
	}
	
	@Test
	@Order(2)
	public void testGetTradeDetailsPresent() {
		Trade fetchTrade = tradeStoreService.testGetTradeDetails(tradeExisting);
		assertEquals(tradeExisting.getTradeId(), fetchTrade.getTradeId());
		assertEquals(tradeExisting.getBookId(), fetchTrade.getBookId());
		assertEquals(tradeExisting.getTradeVersion(), fetchTrade.getTradeVersion());
		assertEquals(tradeExisting.getCounterParty(), fetchTrade.getCounterParty());
		assertEquals(tradeExisting.getMaturityDate(), fetchTrade.getMaturityDate());
	}
	
	@Test
	@Order(3)
	public void testSaveTradeDetailsExists() {
		Trade trade =new Trade();
		trade.setTradeId("T1");
		trade.setTradeVersion(1);
		trade.setCounterParty("CP-1");
		trade.setBookId("B1");
		trade.setMaturityDate(LocalDate.of(2022, 12, 12));
		trade.setCreateDate(LocalDate.now());
		trade.setExpiryFlag('N');
		assertEquals(ApplicationConstants.TRADE_SAVED_SUCCESS, tradeStoreService.saveTradeDetails(trade));
	}
	
	@Test
	@Order(4)
	public void testSaveTradeDetailsNotExists() {
		assertEquals(ApplicationConstants.TRADE_SAVED_SUCCESS, tradeStoreService.saveTradeDetails(newTrade));
	}
	
	@Test
	@Order(5)
	public void testSaveTradeDetailsMaturityDatePast() {
		newTrade.setMaturityDate(LocalDate.of(2017, 4, 6));
		newTrade.setCreateDate(LocalDate.now());
		Exception exception = assertThrows(MaturityDateException.class, ()->{
			tradeStoreService.saveTradeDetails(newTrade);
		});
		assertEquals(ApplicationConstants.MATURITY_PAST_DATE, exception.getMessage());
	}
	
	@Test
	@Order(6)
	public void testSaveTradeDetailsMaturityDateSystem() {
		newTrade.setMaturityDate(LocalDate.now());
		newTrade.setCreateDate(LocalDate.now());
		assertEquals(ApplicationConstants.TRADE_SAVED_SUCCESS, tradeStoreService.saveTradeDetails(newTrade));
	}
	
	@Test
	@Order(7)
	public void testSaveTradeDetailsMaturityDateFuture() {
		newTrade.setMaturityDate(LocalDate.of(2040, 4, 6));
		newTrade.setCreateDate(LocalDate.now());
		assertEquals(ApplicationConstants.TRADE_SAVED_SUCCESS, tradeStoreService.saveTradeDetails(newTrade));
	}
	
	@Test
	@Order(8)
	public void testSaveTradeDetailsWithLowerVersion() {
		newTrade.setTradeVersion(2);
		newTrade.setCounterParty("CP-2");
		Exception exception =assertThrows(LowerTradeVersionException.class, ()->{
			tradeStoreService.saveTradeDetails(newTrade);
		});
		assertEquals(ApplicationConstants.LOWER_VERSION_TRADE, exception.getMessage());
	
	}
	
	@Test
	@Order(9)
	public void testSaveTradeDetailsWithSameVersion() {
		newTrade.setMaturityDate(LocalDate.of(2040, 4, 6));
		newTrade.setCreateDate(LocalDate.now());
		assertEquals(ApplicationConstants.TRADE_SAVED_SUCCESS, tradeStoreService.saveTradeDetails(newTrade));
		Trade fetchTrade = tradeStoreService.testGetTradeDetails(newTrade);
		assertEquals(newTrade.getTradeId(), fetchTrade.getTradeId());
		assertEquals(newTrade.getBookId(), fetchTrade.getBookId());
		assertEquals(newTrade.getTradeVersion(), fetchTrade.getTradeVersion());
		assertEquals(newTrade.getCounterParty(), fetchTrade.getCounterParty());
		assertEquals(newTrade.getMaturityDate(), fetchTrade.getMaturityDate());
	}
	
	@Test
	@Order(10)
	public void testSaveTradeDetailsWithHigherVersion() {
		newTrade.setTradeVersion(5);
		newTrade.setCounterParty("CP-5");
		assertEquals(ApplicationConstants.TRADE_SAVED_SUCCESS, tradeStoreService.saveTradeDetails(newTrade));
		Trade fetchTrade = tradeStoreService.testGetTradeDetails(newTrade);
		assertEquals(newTrade.getTradeId(), fetchTrade.getTradeId());
		assertEquals(newTrade.getBookId(), fetchTrade.getBookId());
		assertEquals(newTrade.getTradeVersion(), fetchTrade.getTradeVersion());
		assertEquals(newTrade.getCounterParty(), fetchTrade.getCounterParty());
		assertEquals(newTrade.getMaturityDate(), fetchTrade.getMaturityDate());
	}
	@Test
	@Order(11)
	public void testFetchAllTrades() {
		List<Trade> lsOfAllTrades = tradeStoreService.fetchAllTrades();
		assertNotNull(lsOfAllTrades);
	}

}

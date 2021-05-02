package com.investment.tradestore.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.investment.tradestore.exception.NoRecordFoundException;
import com.investment.tradestore.exception.RecordAlreadyPresentException;
import com.investment.tradestore.model.Trade;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class TestTradeStoreRepository {
	
	@Autowired
	private TradeStoreRepository tradeStoreRepository;
	
	private Trade tradeExisting;
	private Trade newTrade;

	@Before
	public void setUp() {
		
		tradeExisting =new Trade();
		tradeExisting.setTradeId("T2");
		tradeExisting.setTradeVersion(1);
		tradeExisting.setCounterParty("CP-1");
		tradeExisting.setBookId("B1");
		tradeExisting.setMaturityDate(LocalDate.of(2024, 12, 12));
		tradeExisting.setCreateDate(LocalDate.now());
		tradeExisting.setExpiryFlag('N');
		
		newTrade =new Trade();
		newTrade.setTradeId("T4");
		newTrade.setTradeVersion(4);
		newTrade.setCounterParty("CP-4");
		newTrade.setBookId("B3");
		newTrade.setMaturityDate(LocalDate.of(2030, 12, 12));
		newTrade.setCreateDate(LocalDate.now());
		newTrade.setExpiryFlag('N');
		
	}
	
	@Test
	@Order(1)
	public void testCheckRecordAvailableNotPresent() {
		assertEquals(false, tradeStoreRepository.checkRecordAvailable(newTrade));
	}
	
	@Test
	@Order(2)
	public void testGetSpecificTradePresent() {
		assertNotNull(tradeStoreRepository.getSpecificTrade(tradeExisting));
	}
	
	@Test
	@Order(3)
	public void testGetSpecificTradeNotPresent() {
		Trade trade =new Trade();
		trade.setTradeId("T7");
		trade.setTradeVersion(1);
		trade.setCounterParty("CP-1");
		trade.setBookId("B1");
		trade.setMaturityDate(LocalDate.of(2030, 12, 12));
		trade.setCreateDate(LocalDate.now());
		trade.setExpiryFlag('N');
		
		Exception exception =assertThrows(NoRecordFoundException.class, ()->{
			tradeStoreRepository.getSpecificTrade(trade);
		});
		assertEquals(ApplicationConstants.NO_RECORD_FOUND, exception.getMessage());
	}
	
	@Test
	@Order(4)
	public void testSaveTrade() {
		assertEquals(ApplicationConstants.TRADE_SAVED_SUCCESS, tradeStoreRepository.saveTrade(newTrade));
		Trade savedTrade = tradeStoreRepository.getSpecificTrade(newTrade);
		assertEquals(newTrade.getTradeId(), savedTrade.getTradeId());
		assertEquals(newTrade.getTradeVersion(), savedTrade.getTradeVersion());
		assertEquals(newTrade.getCounterParty(), savedTrade.getCounterParty());
		assertEquals(newTrade.getBookId(), savedTrade.getBookId());
		assertEquals(newTrade.getMaturityDate(), savedTrade.getMaturityDate());
	}

	
	
	@Test
	@Order(5)
	public void testSaveTradeDataExist() {
		Exception exception =assertThrows(RecordAlreadyPresentException.class, ()->{
			tradeStoreRepository.saveTrade(tradeExisting);
		});
		assertEquals(ApplicationConstants.TRADE_EXIST, exception.getMessage());
	}
	
	@Test
	@Order(6)
	public void testCheckRecordAvailablePresent() {
		assertEquals(true, tradeStoreRepository.checkRecordAvailable(tradeExisting));
	}
	
	
	@Test
	@Order(7)
	public void testCheckTradeVersionLower() {
		tradeExisting.setTradeVersion(0);
		tradeExisting.setCounterParty("CP-0");
		assertEquals(true, tradeStoreRepository.checkLowerTradeVersion(tradeExisting));
	}

	@Test
	@Order(8)
	public void testCheckTradeVersionSame() {
		newTrade.setMaturityDate(LocalDate.now());
		newTrade.setCreateDate(LocalDate.now());
		assertEquals(false, tradeStoreRepository.checkLowerTradeVersion(newTrade));
		Trade savedTrade = tradeStoreRepository.getSpecificTrade(newTrade);
		assertEquals(newTrade.getTradeVersion(), savedTrade.getTradeVersion());
	}
	
	@Test
	@Order(9)
	public void testCheckTradeVersionHigher() {
		tradeExisting.setTradeVersion(5);
		tradeExisting.setCounterParty("CP-5");
		assertEquals(false, tradeStoreRepository.checkLowerTradeVersion(tradeExisting));
	}
	
	@Test
	@Order(10)
	public void testUpdateTrade() {
		tradeExisting.setMaturityDate(LocalDate.of(2025, 8, 29));
		assertEquals(ApplicationConstants.TRADE_SAVED_SUCCESS, tradeStoreRepository.updateTrade(tradeExisting));
		Trade savedTrade = tradeStoreRepository.getSpecificTrade(tradeExisting);
		assertEquals(tradeExisting.getMaturityDate(), savedTrade.getMaturityDate());
	}
	
	@Test
	@Order(11)
	public void testUpdateTradeNoRecordFound() {
		Trade trade =new Trade();
		trade.setTradeId("T11");
		trade.setTradeVersion(4);
		trade.setCounterParty("CP-4");
		trade.setBookId("B5");
		trade.setMaturityDate(LocalDate.of(2030, 12, 12));
		trade.setCreateDate(LocalDate.now());
		trade.setExpiryFlag('N');
		Exception exception =assertThrows(NoRecordFoundException.class, ()->{
			tradeStoreRepository.updateTrade(trade);
		});
		assertEquals(ApplicationConstants.NO_RECORD_FOUND, exception.getMessage());
	}
	
	@Test
	@Order(12)
	public void testCheckMaturityDatePast() {
		tradeExisting.setMaturityDate(LocalDate.of(2009, 11, 29));
		assertEquals(ApplicationConstants.TRADE_SAVED_SUCCESS, tradeStoreRepository.updateTrade(tradeExisting));
		Trade savedTrade = tradeStoreRepository.getSpecificTrade(tradeExisting);
		assertEquals(tradeExisting.getMaturityDate(), savedTrade.getMaturityDate());
	}
	
	@Test
	@Order(13)
	public void testCheckMaturityDateToday() {
		tradeExisting.setMaturityDate(LocalDate.now());
		assertEquals(ApplicationConstants.TRADE_SAVED_SUCCESS, tradeStoreRepository.updateTrade(tradeExisting));
		Trade savedTrade = tradeStoreRepository.getSpecificTrade(tradeExisting);
		assertEquals(tradeExisting.getMaturityDate(), savedTrade.getMaturityDate());
	}
	
	@Test
	@Order(14)
	public void testCheckMaturityDateFuture() {
		tradeExisting.setMaturityDate(LocalDate.of(2029, 11, 29));
		assertEquals(ApplicationConstants.TRADE_SAVED_SUCCESS, tradeStoreRepository.updateTrade(tradeExisting));
		Trade savedTrade = tradeStoreRepository.getSpecificTrade(tradeExisting);
		assertEquals(tradeExisting.getMaturityDate(), savedTrade.getMaturityDate());
	}
	
	@Test
	@Order(15)
	public void testFetchTrades() {
		List<Trade> lsOfAllTrades = tradeStoreRepository.fetchTrades();
		assertNotNull(lsOfAllTrades);
	}
	
	@Test
	@Order(16)
	public void testUpdateExpiryFlag() {
		assertEquals(ApplicationConstants.EXPIRY_FLAG_UPDATE_MSG, tradeStoreRepository.updateExpiryFlag());
	}
}

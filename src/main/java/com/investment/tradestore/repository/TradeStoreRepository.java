package com.investment.tradestore.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.investment.tradestore.constants.ApplicationConstants;
import com.investment.tradestore.exception.NoRecordFoundException;
import com.investment.tradestore.exception.RecordAlreadyPresentException;
import com.investment.tradestore.model.Trade;

@Component
public class TradeStoreRepository {
	
	private List<Trade> lsOfTrades=null;
	
	public TradeStoreRepository() {
		lsOfTrades = new ArrayList<>();
		Trade tradeRecord1 = new Trade();
		tradeRecord1.setTradeId("T1");
		tradeRecord1.setTradeVersion(1);
		tradeRecord1.setCounterParty("CP-1");
		tradeRecord1.setBookId("B1");
		tradeRecord1.setMaturityDate(LocalDate.of(2022, 12, 12));
		tradeRecord1.setCreateDate(LocalDate.now());
		tradeRecord1.setExpiryFlag('N');
		
		Trade tradeRecord2 = new Trade();
		tradeRecord2.setTradeId("T2");
		tradeRecord2.setTradeVersion(2);
		tradeRecord2.setCounterParty("CP-2");
		tradeRecord2.setBookId("B1");
		tradeRecord2.setMaturityDate(LocalDate.of(2023, 12, 12));
		tradeRecord2.setCreateDate(LocalDate.now());
		tradeRecord2.setExpiryFlag('N');
		
		Trade tradeRecord3 = new Trade();
		tradeRecord3.setTradeId("T2");
		tradeRecord3.setTradeVersion(1);
		tradeRecord3.setCounterParty("CP-1");
		tradeRecord3.setBookId("B1");
		tradeRecord3.setMaturityDate(LocalDate.of(2024, 12, 12));
		tradeRecord3.setCreateDate(LocalDate.now());
		tradeRecord3.setExpiryFlag('N');
		
		Trade tradeRecord4 = new Trade();
		tradeRecord4.setTradeId("T3");
		tradeRecord4.setTradeVersion(3);
		tradeRecord4.setCounterParty("CP-3");
		tradeRecord4.setBookId("B2");
		tradeRecord4.setMaturityDate(LocalDate.of(2020, 12, 12));
		tradeRecord4.setCreateDate(LocalDate.now());
		tradeRecord4.setExpiryFlag('N');
		
		lsOfTrades.add(tradeRecord1);
		lsOfTrades.add(tradeRecord2);
		lsOfTrades.add(tradeRecord3);
		lsOfTrades.add(tradeRecord4);
	}

	public boolean checkRecordAvailable(Trade trade) {
		
		List<Trade> lsTradeExist = lsOfTrades.stream()
				.filter(t->t.getTradeId().equals(trade.getTradeId()) 
						&& t.getTradeVersion() == trade.getTradeVersion() 
						&& t.getCounterParty().equals(trade.getCounterParty()) 
								&& t.getBookId().equals(trade.getBookId()))
				.collect(Collectors.toList());
		
		return lsTradeExist.isEmpty()?false:true;
		
	}

	public String saveTrade(Trade trade) {
		if(checkRecordAvailable(trade)) {
			throw new RecordAlreadyPresentException(ApplicationConstants.TRADE_EXIST);
		}
		trade.setCreateDate(LocalDate.now());
		trade.setExpiryFlag('N');
		lsOfTrades.add(trade);
		return ApplicationConstants.TRADE_SAVED_SUCCESS;
	}
	
	public Trade getSpecificTrade(Trade trade){
		Optional<Trade> specificTrade = lsOfTrades.stream()
				.filter(t->t.getTradeId().equals(trade.getTradeId()) 
				&& t.getTradeVersion() == trade.getTradeVersion() 
				&& t.getCounterParty().equals(trade.getCounterParty()) 
						&& t.getBookId().equals(trade.getBookId()))
		.findFirst();
		return specificTrade.orElseThrow(()->new NoRecordFoundException(ApplicationConstants.NO_RECORD_FOUND));
	}
	
	public boolean checkLowerTradeVersion(Trade trade) {
		if(lsOfTrades.isEmpty())
			return false;
		List<Trade> lsOfTradeWithSametrade = lsOfTrades.stream()
				.filter(t->t.getTradeId().equals(trade.getTradeId()) 
						&& t.getBookId().equals(trade.getBookId()))
				.collect(Collectors.toList());
		if(lsOfTradeWithSametrade.isEmpty())
			return false;
		Trade maxVersionTrade = lsOfTradeWithSametrade.stream()
				.max(Comparator.comparing(tradeRec->tradeRec.getTradeVersion())).get();
		
		return trade.getTradeVersion() < maxVersionTrade.getTradeVersion();
	}

	public String updateTrade(Trade trade) {
		int i = 0;
		if(checkRecordAvailable(trade)) {
			for(Trade tradeExist : lsOfTrades) {
				if(tradeExist.getTradeId().equals(trade.getTradeId())
						&& tradeExist.getTradeVersion() == trade.getTradeVersion()
						&& tradeExist.getCounterParty().equals(trade.getCounterParty()) 
						&& tradeExist.getBookId().equals(trade.getBookId())) {
					break;
				}
				i++;
			}
			
			lsOfTrades.get(i).setMaturityDate(trade.getMaturityDate());
			lsOfTrades.get(i).setCreateDate(LocalDate.now());
			lsOfTrades.get(i).setExpiryFlag('N');
			return ApplicationConstants.TRADE_SAVED_SUCCESS;
		}else {
			throw new NoRecordFoundException(ApplicationConstants.NO_RECORD_FOUND);
		}
			
	}
	
	public boolean checkMaturityDate(LocalDate maturityDate) {
		return maturityDate.isBefore(LocalDate.now());
	}

	public List<Trade> fetchTrades() {
		return lsOfTrades;
	}
	
	public String updateExpiryFlag() {
		
		List<Integer> lsTradeExpired = new ArrayList<>();
		int index = 0;
		
		for(Trade t : lsOfTrades) {
			if(t.getMaturityDate().isBefore(LocalDate.now()) && t.getExpiryFlag() == 'N') {
				lsTradeExpired.add(index);
			}
			index++;
		}
		for(int i : lsTradeExpired) {
			lsOfTrades.get(i).setExpiryFlag('Y');
		}
		return ApplicationConstants.EXPIRY_FLAG_UPDATE_MSG;
	}
	

}

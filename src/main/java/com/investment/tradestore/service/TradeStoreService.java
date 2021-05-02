package com.investment.tradestore.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.investment.tradestore.constants.ApplicationConstants;
import com.investment.tradestore.exception.LowerTradeVersionException;
import com.investment.tradestore.exception.MaturityDateException;
import com.investment.tradestore.model.Trade;
import com.investment.tradestore.repository.TradeStoreRepository;

@Service
public class TradeStoreService {
	
	private final TradeStoreRepository tradeStoreRepository;
	
	public TradeStoreService(TradeStoreRepository tradeStoreRepository) {
		this.tradeStoreRepository = tradeStoreRepository;
	}
	
	/** To save trades
	 * @param trade trade details
	 * @return message response
	 */
	public String saveTradeDetails(Trade trade) {
		
		if(tradeStoreRepository.checkMaturityDate(trade.getMaturityDate())) {
			throw new MaturityDateException(ApplicationConstants.MATURITY_PAST_DATE);
		}
		
		if(tradeStoreRepository.checkRecordAvailable(trade)) {
			return tradeStoreRepository.updateTrade(trade);
		}
		else {
			if(tradeStoreRepository.checkLowerTradeVersion(trade)) {
				throw new LowerTradeVersionException(ApplicationConstants.LOWER_VERSION_TRADE);
			}
			else {
				tradeStoreRepository.saveTrade(trade);
			}
		}
		return ApplicationConstants.TRADE_SAVED_SUCCESS;
	}

	/** To fetch specific trades
	 * @param trade trade
	 * @return trade specific trade details
	 */
	public Trade testGetTradeDetails(Trade trade) {
		return tradeStoreRepository.getSpecificTrade(trade);
	}

	/** To fetch all trades stored
	 * @return list of trades
	 */
	public List<Trade> fetchAllTrades() {
		return tradeStoreRepository.fetchTrades();
	}

}

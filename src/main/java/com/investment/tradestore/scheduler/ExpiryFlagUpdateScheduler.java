package com.investment.tradestore.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.investment.tradestore.repository.TradeStoreRepository;

@Component
public class ExpiryFlagUpdateScheduler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpiryFlagUpdateScheduler.class);
	
	private TradeStoreRepository tradeStoreRepository;
	
	public ExpiryFlagUpdateScheduler(TradeStoreRepository tradeStoreRepository) {
		this.tradeStoreRepository = tradeStoreRepository;
	}
	
	@Scheduled(cron="0 0/5 * 1/1 * *")
	public void updateExpiryFlag() {
		
		LOGGER.info("Scheduler started to update expiry flag for all the trades");
		tradeStoreRepository.updateExpiryFlag();
		LOGGER.info("Expiry flag got updated for all the trades");
	}

}

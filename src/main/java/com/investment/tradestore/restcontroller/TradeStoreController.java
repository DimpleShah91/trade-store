package com.investment.tradestore.restcontroller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.investment.tradestore.constants.ApplicationApiConstants;
import com.investment.tradestore.model.Trade;
import com.investment.tradestore.service.TradeStoreService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1")
@Api(value="TradeStore")
public class TradeStoreController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeStoreController.class);
	
	private TradeStoreService tradeStoreService;
	
	public TradeStoreController(TradeStoreService tradeStoreService) {
		this.tradeStoreService = tradeStoreService;
	}
	
	/** To fetch all the trade details
	 * @return list of trade details
	 */
	@GetMapping(ApplicationApiConstants.FETCH_ALL_TRADE_URI)
	public ResponseEntity<List<Trade>> fetchTradeDetails(){
		return ResponseEntity.ok(tradeStoreService.fetchAllTrades());
	}

	/** To save trade details 
	 * @param trade trade details 
	 * @return response message
	 */
	@ApiOperation(value="Store trade information", response = ResponseEntity.class)
	@PostMapping(value=ApplicationApiConstants.SAVE_TRADE_URI, produces=ApplicationApiConstants.CONTENT_FORMAT)
	public ResponseEntity<String> saveTradeInformation(@Valid @RequestBody Trade trade){
		LOGGER.info("To save trades");
		String response = tradeStoreService.saveTradeDetails(trade);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
}

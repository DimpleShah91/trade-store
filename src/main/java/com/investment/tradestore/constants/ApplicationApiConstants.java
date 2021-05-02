package com.investment.tradestore.constants;

import org.springframework.http.MediaType;

public class ApplicationApiConstants {
	private ApplicationApiConstants() {

	}

	public static final String SAVE_TRADE_URI = "/save";
	public static final String FETCH_ALL_TRADE_URI = "/fetch";
	public static final String CONTENT_FORMAT = MediaType.APPLICATION_JSON_VALUE;
}

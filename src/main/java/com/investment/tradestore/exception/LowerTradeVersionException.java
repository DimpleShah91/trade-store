package com.investment.tradestore.exception;

public class LowerTradeVersionException extends RuntimeException{

	private static final long serialVersionUID = 284317455599383450L;

	public LowerTradeVersionException(String message) {
		super(message);
	}
}

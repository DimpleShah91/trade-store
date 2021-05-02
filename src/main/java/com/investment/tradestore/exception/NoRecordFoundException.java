package com.investment.tradestore.exception;

public class NoRecordFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 3119665156639141095L;

	public NoRecordFoundException(String message) {
		super(message);
	}

}

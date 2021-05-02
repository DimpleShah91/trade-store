package com.investment.tradestore.exception;

public class RecordAlreadyPresentException extends RuntimeException{

	private static final long serialVersionUID = -990999324818569925L;

	public RecordAlreadyPresentException(String message) {
		super(message);
	}
}

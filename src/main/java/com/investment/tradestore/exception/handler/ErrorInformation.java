package com.investment.tradestore.exception.handler;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorInformation {

	private LocalDate date;
	private String errorMessage;
	public ErrorInformation(LocalDate date, String errorMessage) {
		super();
		this.date = date;
		this.errorMessage = errorMessage;
	}
	
	
}

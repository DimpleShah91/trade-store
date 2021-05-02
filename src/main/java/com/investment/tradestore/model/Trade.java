package com.investment.tradestore.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trade {
	
	@NotBlank(message="Trade Id cannot be null")
	private String tradeId;
	@NotBlank(message="Trade version cannot be null")
	private Integer tradeVersion;
	@NotBlank(message="Counter party Id cannot be null")
	private String counterParty;
	@NotBlank(message="Book Id cannot be null")
	private String bookId;
	private LocalDate maturityDate;
	private LocalDate createDate;
	private char expiryFlag;

}

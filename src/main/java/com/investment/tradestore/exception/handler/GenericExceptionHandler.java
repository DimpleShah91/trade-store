package com.investment.tradestore.exception.handler;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.investment.tradestore.exception.LowerTradeVersionException;
import com.investment.tradestore.exception.MaturityDateException;
import com.investment.tradestore.exception.NoRecordFoundException;
import com.investment.tradestore.exception.RecordAlreadyPresentException;

@RestControllerAdvice
public class GenericExceptionHandler {
	
	@ExceptionHandler(NoRecordFoundException.class)
	public ResponseEntity<ErrorInformation> noRecordFoundException(NoRecordFoundException ex){
		ErrorInformation errorInformation = new ErrorInformation(LocalDate.now(), ex.getMessage());
		return new ResponseEntity<>(errorInformation, HttpStatus.NOT_FOUND);
		
	}
	@ExceptionHandler(RecordAlreadyPresentException.class)
	public ResponseEntity<ErrorInformation> noRecordFoundException(RecordAlreadyPresentException ex){
		ErrorInformation errorInformation = new ErrorInformation(LocalDate.now(), ex.getMessage());
		return new ResponseEntity<>(errorInformation, HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(LowerTradeVersionException.class)
	public ResponseEntity<ErrorInformation> lowerTradeVersionException(LowerTradeVersionException ex){
		ErrorInformation errorInformation = new ErrorInformation(LocalDate.now(), ex.getMessage());
		return new ResponseEntity<>(errorInformation, HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(MaturityDateException.class)
	public ResponseEntity<ErrorInformation> maturityDateException(MaturityDateException ex){
		ErrorInformation errorInformation = new ErrorInformation(LocalDate.now(), ex.getMessage());
		return new ResponseEntity<>(errorInformation, HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInformation> handleException(Exception ex){
		ErrorInformation errorInformation = new ErrorInformation(LocalDate.now(), ex.getMessage());
		return new ResponseEntity<>(errorInformation, HttpStatus.BAD_REQUEST);
		
	}

}

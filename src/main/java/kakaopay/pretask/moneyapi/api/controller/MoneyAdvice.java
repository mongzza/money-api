package kakaopay.pretask.moneyapi.api.controller;

import kakaopay.pretask.moneyapi.api.dto.ErrorResponse;
import kakaopay.pretask.moneyapi.api.exception.MoneyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MoneyAdvice {

	@ExceptionHandler(MoneyException.class)
	public ResponseEntity<ErrorResponse> moneyExceptionHandler(MoneyException e) {
		ErrorResponse response = new ErrorResponse(e.getCode());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> otherExceptionHandler(Exception e) {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}

package kakaopay.pretask.moneyapi.api.controller;

import kakaopay.pretask.moneyapi.api.dto.ErrorResponse;
import kakaopay.pretask.moneyapi.api.exception.MoneyErrorCode;
import kakaopay.pretask.moneyapi.api.exception.MoneyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MoneyAdvice {

	@ExceptionHandler(MoneyException.class)
	public ResponseEntity<ErrorResponse> moneyExceptionHandler(MoneyException e) {
		return new ResponseEntity<>(new ErrorResponse(e.getCode()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> otherExceptionHandler(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<>(new ErrorResponse(MoneyErrorCode.InternalError), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}

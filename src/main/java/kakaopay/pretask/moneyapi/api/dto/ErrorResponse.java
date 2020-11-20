package kakaopay.pretask.moneyapi.api.dto;

import kakaopay.pretask.moneyapi.api.exception.MoneyErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
	private int code;
	private String message;

	public ErrorResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public ErrorResponse(MoneyErrorCode error) {
		this.code = error.getCode();
		this.message = error.getMessage();
	}
}

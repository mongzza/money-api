package kakaopay.pretask.moneyapi.api.dto;

import kakaopay.pretask.moneyapi.api.exception.MoneyErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
	private final int code;
	private final String message;

	public ErrorResponse(MoneyErrorCode error) {
		this.code = error.getCode();
		this.message = error.getMessage();
	}
}

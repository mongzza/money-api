package kakaopay.pretask.moneyapi.api.dto;

import lombok.Getter;

@Getter
public class MoneySpreadResponse {
	private final String token;

	public MoneySpreadResponse(String token) {
		this.token = token;
	}
}

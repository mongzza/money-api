package kakaopay.pretask.moneyapi.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneySpreadResponse {
	private String token;

	public MoneySpreadResponse(String token) {
		this.token = token;
	}
}

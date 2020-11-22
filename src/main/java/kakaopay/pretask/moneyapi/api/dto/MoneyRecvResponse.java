package kakaopay.pretask.moneyapi.api.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MoneyRecvResponse {
	private final BigDecimal money;

	public MoneyRecvResponse(BigDecimal money) {
		this.money = money;
	}
}

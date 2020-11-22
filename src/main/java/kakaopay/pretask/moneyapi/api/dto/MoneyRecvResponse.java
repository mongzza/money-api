package kakaopay.pretask.moneyapi.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MoneyRecvResponse {
	private BigDecimal money;

	public MoneyRecvResponse(BigDecimal money) {
		this.money = money;
	}
}

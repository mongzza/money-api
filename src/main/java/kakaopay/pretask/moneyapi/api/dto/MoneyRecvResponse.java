package kakaopay.pretask.moneyapi.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyRecvResponse {
	private long money;

	public MoneyRecvResponse(long money) {
		this.money = money;
	}
}

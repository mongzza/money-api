package kakaopay.pretask.moneyapi.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MoneySpreadRequest {
	private BigDecimal money;
	private Long headCount;

	@Builder
	public MoneySpreadRequest(BigDecimal money, Long headCount) {
		this.money = money;
		this.headCount = headCount;
	}
}

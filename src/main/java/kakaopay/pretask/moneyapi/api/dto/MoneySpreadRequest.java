package kakaopay.pretask.moneyapi.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MoneySpreadRequest {
	private Long userId;
	private String roomId;
	private BigDecimal money;
	private Long headCount;
}

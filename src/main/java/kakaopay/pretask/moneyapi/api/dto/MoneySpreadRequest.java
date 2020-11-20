package kakaopay.pretask.moneyapi.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneySpreadRequest {
	private Long userId;
	private String roomId;
	private Long money;
	private int headCount;
}

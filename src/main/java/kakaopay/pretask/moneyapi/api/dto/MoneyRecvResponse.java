package kakaopay.pretask.moneyapi.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyRecvResponse extends CommonResponse {
	private Long money;
}

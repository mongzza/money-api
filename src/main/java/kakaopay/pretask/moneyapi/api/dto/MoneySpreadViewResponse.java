package kakaopay.pretask.moneyapi.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MoneySpreadViewResponse extends CommonResponse {
	private LocalDateTime createdDate;
	private Long money;
	private Long receivedMoney;
	private List<ReceivedInfoResponse> receivedInfo;
}

@Getter
@Setter
class ReceivedInfoResponse {
	private Long userId;
	private Long money;
}

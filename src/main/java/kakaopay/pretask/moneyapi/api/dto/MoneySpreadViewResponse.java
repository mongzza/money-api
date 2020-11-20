package kakaopay.pretask.moneyapi.api.dto;

import kakaopay.pretask.moneyapi.domain.event.MoneyEvent;
import kakaopay.pretask.moneyapi.domain.event.SubMoneyEvent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MoneySpreadViewResponse extends CommonResponse {
	private LocalDateTime createdDate;
	private Long money;
	private Long receivedMoney;
	private List<ReceivedInfoResponse> receivedInfo;

	public MoneySpreadViewResponse() {}

	public MoneySpreadViewResponse(MoneyEvent event) {
		this.createdDate = event.getCreatedDate();
		this.money = event.getMoney();
		this.receivedInfo = new ArrayList<>();
		for (int i = 0; i < event.getSubEvents().size(); i++) {
			new ReceivedInfoResponse(event.getSubEvents().get(i));
		}
	}
}

@Getter
@Setter
class ReceivedInfoResponse {
	private Long userId;
	private Long money;

	public ReceivedInfoResponse(SubMoneyEvent subEvent) {
		this.userId = subEvent.getUser().getUserId();
		this.money = subEvent.getMoney();
	}
}

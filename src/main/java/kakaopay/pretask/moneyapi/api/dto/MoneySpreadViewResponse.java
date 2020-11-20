package kakaopay.pretask.moneyapi.api.dto;

import kakaopay.pretask.moneyapi.domain.event.MoneyEvent;
import kakaopay.pretask.moneyapi.domain.event.SubMoneyEvent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MoneySpreadViewResponse {
	private LocalDateTime createdDate;
	private Long money;
	private Long receivedMoney;
	private List<ReceivedInfoResponse> receivedInfo;

	public MoneySpreadViewResponse(MoneyEvent event, Long receivedMoney) {
		this.createdDate = event.getCreatedDate();
		this.money = event.getMoney();
		this.receivedMoney = receivedMoney;
		this.receivedInfo = new ArrayList<>();
		event.getSubEvents().stream()
				.filter(subEvent -> subEvent.getAssignedYn() == 'Y')
				.forEach(subEvent -> this.receivedInfo.add(new ReceivedInfoResponse(subEvent)));
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

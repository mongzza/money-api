package kakaopay.pretask.moneyapi.api.dto;

import kakaopay.pretask.moneyapi.domain.event.ReceivedMoney;
import kakaopay.pretask.moneyapi.domain.event.SpreadMoney;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MoneySpreadViewResponse {
	private LocalDateTime createdDate;
	private BigDecimal money;
	private BigDecimal receivedMoney;
	private List<ReceivedInfoResponse> receivedInfo;

	public MoneySpreadViewResponse(SpreadMoney event) {
		this.createdDate = event.getCreatedDate();
		this.money = event.getMoney();
		this.receivedMoney = event.receivedMoniesSum();
		this.receivedInfo = new ArrayList<>();

		event.getReceivedMonies()
				.forEach(received -> this.receivedInfo.add(new ReceivedInfoResponse(received)));
	}

}

@Getter
@Setter
class ReceivedInfoResponse {
	private Long userId;
	private BigDecimal money;

	public ReceivedInfoResponse(ReceivedMoney received) {
		this.userId = received.getUser().getUserId();
		this.money = received.getMoney();
	}
}

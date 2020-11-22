package kakaopay.pretask.moneyapi.api.dto;

import kakaopay.pretask.moneyapi.domain.event.ReceivedMoney;
import kakaopay.pretask.moneyapi.domain.event.SpreadMoney;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MoneySpreadViewResponse {
	private final LocalDateTime createdDate;
	private final BigDecimal money;
	private final BigDecimal receivedMoney;
	private final List<ReceivedInfoResponse> receivedInfo;

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
class ReceivedInfoResponse {
	private final Long userId;
	private final BigDecimal money;

	public ReceivedInfoResponse(ReceivedMoney received) {
		this.userId = received.getUser().getUserId();
		this.money = received.getMoney();
	}
}

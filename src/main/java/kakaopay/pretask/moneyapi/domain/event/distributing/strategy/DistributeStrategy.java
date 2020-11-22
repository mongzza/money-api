package kakaopay.pretask.moneyapi.domain.event.distributing.strategy;

import java.math.BigDecimal;

public interface DistributeStrategy {
	BigDecimal distribute(Long remainedHeadCount, BigDecimal remainedMoney);
}

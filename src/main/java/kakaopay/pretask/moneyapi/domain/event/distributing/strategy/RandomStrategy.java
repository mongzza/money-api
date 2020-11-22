package kakaopay.pretask.moneyapi.domain.event.distributing.strategy;

import java.math.BigDecimal;

public class RandomStrategy implements DistributeStrategy{

	@Override
	public BigDecimal distribute(Long remainedHeadCount, BigDecimal remainedMoney) {
		if (remainedHeadCount == 1) {
			return remainedMoney;
		}
		BigDecimal operand = remainedMoney.divideToIntegralValue(BigDecimal.valueOf(Math.random() * remainedHeadCount + 1));
		return remainedMoney.subtract(operand);
	}

}

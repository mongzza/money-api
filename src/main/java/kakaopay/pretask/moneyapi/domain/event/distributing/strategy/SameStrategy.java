package kakaopay.pretask.moneyapi.domain.event.distributing.strategy;

import java.math.BigDecimal;

public class SameStrategy implements DistributeStrategy{

	@Override
	public BigDecimal distribute(Long remainedHeadCount, BigDecimal remainedMoney) {
		if (remainedHeadCount == 1) {
			return remainedMoney;
		}

		return remainedMoney.divideToIntegralValue(new BigDecimal(remainedHeadCount));
	}

}

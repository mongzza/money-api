package kakaopay.pretask.moneyapi.domain.event.distributing;

import kakaopay.pretask.moneyapi.domain.event.distributing.strategy.DistributeStrategy;

import java.math.BigDecimal;

public class Distributing {
	private DistributeStrategy strategy;

	public void setStrategy(DistributeStrategy strategy) {
		this.strategy = strategy;
	}

	public BigDecimal distribute(Long remainedHeadCount, BigDecimal remainedMoney) {
		return this.strategy.distribute(remainedHeadCount, remainedMoney);
	}

}

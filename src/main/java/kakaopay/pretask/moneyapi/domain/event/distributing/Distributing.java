package kakaopay.pretask.moneyapi.domain.event.distributing;

import kakaopay.pretask.moneyapi.domain.event.distributing.strategy.DistributeStrategy;

public class Distributing {
	private DistributeStrategy strategy;

	public void setStrategy(DistributeStrategy strategy) {
		this.strategy = strategy;
	}

	public long[] distribute(int headCount, long money) {
		return this.strategy.distribute(headCount, money);
	}

}

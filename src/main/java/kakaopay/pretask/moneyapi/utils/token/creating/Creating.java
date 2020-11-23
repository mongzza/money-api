package kakaopay.pretask.moneyapi.utils.token.creating;

import kakaopay.pretask.moneyapi.utils.token.creating.strategy.CreateStrategy;

public class Creating {
	private CreateStrategy strategy;

	private Creating(CreateStrategy strategy) {
		this.strategy = strategy;
	}

	public static Creating by(CreateStrategy strategy) {
		return new Creating(strategy);
	}

	public String create(int size) {
		return this.strategy.create(size);
	}

}

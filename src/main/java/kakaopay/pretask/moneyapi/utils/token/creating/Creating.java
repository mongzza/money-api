package kakaopay.pretask.moneyapi.utils.token.creating;

import kakaopay.pretask.moneyapi.utils.token.creating.strategy.CreateStrategy;

public class Creating {
	private CreateStrategy strategy;

	public Creating(CreateStrategy strategy) {
		this.strategy = strategy;
	}

	public String create() {
		return this.strategy.create();
	}

}

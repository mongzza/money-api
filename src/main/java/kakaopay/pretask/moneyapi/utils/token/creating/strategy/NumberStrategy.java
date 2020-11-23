package kakaopay.pretask.moneyapi.utils.token.creating.strategy;

public class NumberStrategy implements CreateStrategy {
	@Override
	public String create(int size) {
		StringBuilder token = new StringBuilder();
		for (int i = 0; i < size; i++) {
			token.append((int)(Math.random() * 10) + 1);
		}
		return token.toString();
	}
}

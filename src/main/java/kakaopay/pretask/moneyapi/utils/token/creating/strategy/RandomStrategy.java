package kakaopay.pretask.moneyapi.utils.token.creating.strategy;

public class RandomStrategy implements CreateStrategy {
	private static final int TOKEN_LENGTH = 3;
	private static final int METHOD_COUNT = 3;

	@Override
	public String create() {
		StringBuilder token = new StringBuilder();
		for (int i = 0; i < TOKEN_LENGTH; i++) {
			switch ((int)(Math.random() * METHOD_COUNT)) {
				case 0: token.append(randomUpperCase());
				break;
				case 1: token.append(randomLowerCase());
				break;
				case 2: token.append(randomNumber());
				break;
			}
		}
		return token.toString();
	}

	private char randomUpperCase() {
		return (char) ((int)(Math.random() * 26) + 65);
	}

	private char randomLowerCase() {
		return (char) ((int)(Math.random() * 26) + 97);
	}

	private char randomNumber() {
		return (char) ((int)(Math.random() * 10) + 48);
	}
}

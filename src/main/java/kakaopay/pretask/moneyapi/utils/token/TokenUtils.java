package kakaopay.pretask.moneyapi.utils.token;

import kakaopay.pretask.moneyapi.utils.token.creating.Creating;
import kakaopay.pretask.moneyapi.utils.token.creating.strategy.RandomStrategy;

public class TokenUtils {

	public static String create() {
		Creating newToken = new Creating();
		newToken.setStrategy(new RandomStrategy());
		return newToken.create();
	}
}

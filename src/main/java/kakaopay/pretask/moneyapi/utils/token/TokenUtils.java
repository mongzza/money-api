package kakaopay.pretask.moneyapi.utils.token;

import kakaopay.pretask.moneyapi.utils.token.creating.Creating;
import kakaopay.pretask.moneyapi.utils.token.creating.strategy.CreateStrategy;
import kakaopay.pretask.moneyapi.utils.token.creating.strategy.RandomStrategy;

public class TokenUtils {

	public static String create(int size) {
		return Creating.by(new RandomStrategy())
				.create(size);
	}
}

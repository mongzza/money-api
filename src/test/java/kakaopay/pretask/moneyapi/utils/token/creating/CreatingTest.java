package kakaopay.pretask.moneyapi.utils.token.creating;

import kakaopay.pretask.moneyapi.utils.token.creating.strategy.RandomStrategy;
import kakaopay.pretask.moneyapi.utils.token.creating.strategy.NumberStrategy;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class CreatingTest {
	private static final String numberPattern = Pattern.compile("^[0-9]+$").pattern();
	private static final String randomPattern = Pattern.compile("^[A-Za-z0-9+]*$").pattern();
	private static final int TOKEN_SIZE = 3;

	@DisplayName(value = "생성한 토큰 크기 테스트")
	@Test
	public void testCreatedTokenSize() {
		String randomToken = Creating.by(new RandomStrategy()).create(TOKEN_SIZE);
		String numberToken = Creating.by(new NumberStrategy()).create(TOKEN_SIZE);

		assertThat(randomToken).hasSize(TOKEN_SIZE);
		assertThat(numberToken).hasSize(TOKEN_SIZE);
	}

	@DisplayName(value = "숫자 전략으로 생성한 토큰값 검증")
	@Test
	public void testNumberTokenIsOnlyNumber() {
		String numberToken = Creating.by(new NumberStrategy()).create(TOKEN_SIZE);

		assertThat(numberToken.matches(numberPattern)).isTrue();
	}

	@DisplayName(value = "랜덤 전략으로 생성한 토큰값 검증")
	@Test
	public void testRandomTokenValid() {
		String numberToken = Creating.by(new RandomStrategy()).create(TOKEN_SIZE);

		assertThat(numberToken.matches(randomPattern)).isTrue();
	}
}

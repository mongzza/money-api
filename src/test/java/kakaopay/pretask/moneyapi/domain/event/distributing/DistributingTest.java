package kakaopay.pretask.moneyapi.domain.event.distributing;

import kakaopay.pretask.moneyapi.domain.event.distributing.strategy.RandomStrategy;
import kakaopay.pretask.moneyapi.domain.event.distributing.strategy.SameStrategy;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class DistributingTest {

	@DisplayName(value = "랜덤 분배 전략으로 금액 분배 기능 테스트")
	@ParameterizedTest
	@CsvSource(value = {"5:5000", "3:8500", "2:10000"}, delimiter = ':')
	public void testRandomDistribute(Long remainedHeadCount, BigDecimal remainedMoney) {
		BigDecimal money = Distributing.of(new RandomStrategy())
				.distribute(remainedHeadCount, remainedMoney);

		assertThat(money).isPositive();
	}

	@DisplayName(value = "동등 분배 전략으로 금액 분배 기능 테스트")
	@ParameterizedTest
	@CsvSource(value = {"5:5000", "3:8500", "2:10000"}, delimiter = ':')
	public void testSameDistribute(Long remainedHeadCount, BigDecimal remainedMoney) {
		BigDecimal money = Distributing.of(new SameStrategy())
				.distribute(remainedHeadCount, remainedMoney);

		BigDecimal expected = remainedMoney.divideToIntegralValue(BigDecimal.valueOf(remainedHeadCount));
		assertThat(money).isEqualTo(expected);
	}

	@DisplayName(value = "마지막 한 명에게 분배할 때 전체 금액 반환 여부 테스트")
	@Test
	public void testDistributeOneUser() {
		BigDecimal money = Distributing.of(new RandomStrategy())
				.distribute(1L, BigDecimal.valueOf(3600));

		assertThat(money).isEqualTo(BigDecimal.valueOf(3600));
	}
}

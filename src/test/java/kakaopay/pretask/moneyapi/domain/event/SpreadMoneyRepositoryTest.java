package kakaopay.pretask.moneyapi.domain.event;

import kakaopay.pretask.moneyapi.api.exception.MoneyErrorCode;
import kakaopay.pretask.moneyapi.api.exception.MoneyException;
import kakaopay.pretask.moneyapi.domain.room.Room;
import kakaopay.pretask.moneyapi.domain.user.User;
import kakaopay.pretask.moneyapi.utils.token.TokenUtils;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpreadMoneyRepositoryTest {

	@Autowired
	SpreadMoneyRepository spreadMoneyRepository;

	private User user;
	private Room room;

	@Before
	public void setUp() {
		user = User.builder()
				.userId(10001L)
				.build();
		room = Room.builder()
				.roomId("ROOM1")
				.build();
	}

	@DisplayName(value = "현재 시간 이전까지 생성된 뿌리기 건 개수 조회")
	@Test
	public void testCountByCreatedDateBefore() {
		spreadMoneyRepository.save(SpreadMoney.builder()
				.user(user)
				.room(room)
				.token(TokenUtils.create(3))
				.headCount(3L)
				.money(BigDecimal.valueOf(5000))
				.build());

		long count = spreadMoneyRepository.countByCreatedDateBefore(LocalDateTime.now());

		assertThat(count).isEqualTo(1);
	}

	@DisplayName(value = "뿌리기건의 받기 만료 시간 조회")
	@Test
	void testFindByTokenAndRecvExpDateAfter() {
		String token = TokenUtils.create(3);
		spreadMoneyRepository.save(SpreadMoney.builder()
				.user(user)
				.room(room)
				.token(token)
				.headCount(3L)
				.money(BigDecimal.valueOf(5000))
				.build());

		SpreadMoney spreadMoney = spreadMoneyRepository.findByTokenAndRecvExpDateAfter(token, LocalDateTime.now())
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.ExpireReceiveDate));

		assertThat(spreadMoney.getRecvExpDate()).isEqualTo(spreadMoney.getCreatedDate().plusMinutes(10L));
	}

	@DisplayName(value = "뿌리기건의 조회 만료 시간 조회")
	@Test
	void testFindByTokenAndViewExpDateAfter() {
		String token = TokenUtils.create(3);
		spreadMoneyRepository.save(SpreadMoney.builder()
				.user(user)
				.room(room)
				.token(token)
				.headCount(3L)
				.money(BigDecimal.valueOf(5000))
				.build());

		SpreadMoney spreadMoney = spreadMoneyRepository.findByTokenAndViewExpDateAfter(token, LocalDateTime.now())
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.ExpireViewDate));

		assertThat(spreadMoney.getViewExpDate()).isEqualTo(spreadMoney.getCreatedDate().plusDays(7L));
	}

}

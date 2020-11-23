package kakaopay.pretask.moneyapi.api.service;

import kakaopay.pretask.moneyapi.api.dto.MoneySpreadRequest;
import kakaopay.pretask.moneyapi.api.dto.MoneySpreadViewResponse;
import kakaopay.pretask.moneyapi.api.exception.MoneyException;
import kakaopay.pretask.moneyapi.domain.event.SpreadMoneyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoneyServiceTest {

	private static final Long SPREAD_USER = 10001L;
	private static final String SPREAD_ROOM = "ROOM1";

	private final MoneySpreadRequest request = new MoneySpreadRequest();

	@Autowired
	private MoneyService moneyService;

	@Autowired
	private SpreadMoneyRepository spreadMoneyRepository;

	@Before
	public void setUp() {
		request.setMoney(new BigDecimal(50000));
		request.setHeadCount(4L);
	}

	@DisplayName(value = "뿌리기 API 성공 - 적절한 뿌리기 금액, 인원 입력")
	@Test
	public void testSpreadSuccess() {
		String token = moneyService.spread(SPREAD_USER, SPREAD_ROOM, request);

		assertThat(token).hasSize(3);
		assertThat(spreadMoneyRepository.findByTokenAndViewExpDateAfter(token, LocalDateTime.now()).isPresent())
				.isTrue();
	}

	@DisplayName(value = "뿌리기 API 실패 - 대화방 초과 인원 입력")
	@Test
	public void testSpreadOverHeadCount() {
		request.setHeadCount(10L);

		long before = spreadMoneyRepository.countByCreatedDateBefore(LocalDateTime.now());
		assertThatThrownBy(() -> moneyService.spread(SPREAD_USER, SPREAD_ROOM, request))
				.isInstanceOf(MoneyException.class)
				.hasMessage("받을 인원은 본인을 제외한 대화방 인원 수를 초과할 수 없습니다.");

		long after = spreadMoneyRepository.countByCreatedDateBefore(LocalDateTime.now());
		assertThat(before).isEqualTo(after);
	}

	@DisplayName(value = "뿌리기 API 실패 - 대화방에 포함되어있지 않은 사용자 정보 입력")
	@Test
	public void testSpreadNotExistUserInRoom() {
		request.setHeadCount(2L);

		long before = spreadMoneyRepository.countByCreatedDateBefore(LocalDateTime.now());
		assertThatThrownBy(() -> moneyService.spread(10002L, SPREAD_ROOM, request))
				.isInstanceOf(MoneyException.class)
				.hasMessage("대화방에 존재하지 않는 사용자입니다.");

		long after = spreadMoneyRepository.countByCreatedDateBefore(LocalDateTime.now());
		assertThat(before).isEqualTo(after);
	}

	@DisplayName(value = "받기 API 성공 - 뿌리기한 대화방에 포함된 사용자가 받기 요청")
	@Test
	public void testReceiveSuccess() {
		String token = moneyService.spread(SPREAD_USER, SPREAD_ROOM, request);

		BigDecimal receivedMoney = moneyService.receive(10003L, SPREAD_ROOM, token);

		assertThat(receivedMoney).isPositive();
	}

	@DisplayName(value = "받기 API 실패 - 뿌리기한 사용자가 받기 요청")
	@Test
	public void testReceiveSpreadUserFail() {
		String token = moneyService.spread(SPREAD_USER, SPREAD_ROOM, request);

		assertThatThrownBy(() -> moneyService.receive(SPREAD_USER, SPREAD_ROOM, token))
				.isInstanceOf(MoneyException.class)
				.hasMessage("뿌리기한 사용자는 머니를 받을 수 없습니다.");
	}

	@DisplayName(value = "받기 API 실패 - 이미 받은 사용자가 받기 요청")
	@Test
	public void testReceiveAlreadyUser() {
		String token = moneyService.spread(SPREAD_USER, SPREAD_ROOM, request);

		moneyService.receive(10003L, SPREAD_ROOM, token);

		assertThatThrownBy(() -> moneyService.receive(10003L, SPREAD_ROOM, token))
				.isInstanceOf(MoneyException.class)
				.hasMessage("이미 머니를 받은 사용자입니다.");
	}

	@DisplayName(value = "받기 API 실패 - 받기 가능 인원 초과")
	@Test
	public void testReceiveAlreadyAllUser() {
		request.setHeadCount(2L);
		String token = moneyService.spread(SPREAD_USER, SPREAD_ROOM, request);

		moneyService.receive(10005L, SPREAD_ROOM, token);
		moneyService.receive(10004L, SPREAD_ROOM, token);

		assertThatThrownBy(() -> moneyService.receive(10006L, SPREAD_ROOM, token))
				.isInstanceOf(MoneyException.class)
				.hasMessage("받기 가능 제한 인원이 초과되었습니다.");
	}

	@DisplayName(value = "조회 API 성공")
	@Test
	public void testViewSuccess() {
		String token = moneyService.spread(SPREAD_USER, SPREAD_ROOM, request);

		MoneySpreadViewResponse before = moneyService.viewInfo(SPREAD_USER, SPREAD_ROOM, token);
		assertThat(before.getReceivedInfo().size()).isEqualTo(0);

		moneyService.receive(10005L, SPREAD_ROOM, token);

		MoneySpreadViewResponse after = moneyService.viewInfo(SPREAD_USER, SPREAD_ROOM, token);
		assertThat(after.getReceivedInfo().size()).isEqualTo(1);
		assertThat(after.getReceivedInfo().get(0))
				.hasFieldOrPropertyWithValue("userId", 10005L);
	}

	@DisplayName(value = "조회 API 실패 - 뿌리기 하지 않은 사용자가 조회 요청")
	@Test
	public void testViewNoAuthUserFail() {
		String token = moneyService.spread(SPREAD_USER, SPREAD_ROOM, request);

		assertThatThrownBy(() -> moneyService.viewInfo(10005L, SPREAD_ROOM, token))
				.isInstanceOf(MoneyException.class)
				.hasMessage("뿌리기한 사용자만 조회 가능합니다.");
	}
}

package kakaopay.pretask.moneyapi.domain.event;

import kakaopay.pretask.moneyapi.api.dto.MoneySpreadRequest;
import kakaopay.pretask.moneyapi.api.exception.MoneyErrorCode;
import kakaopay.pretask.moneyapi.api.exception.MoneyException;
import kakaopay.pretask.moneyapi.api.service.MoneyService;
import kakaopay.pretask.moneyapi.domain.user.User;
import kakaopay.pretask.moneyapi.domain.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpreadMoneyTest {

	private static final Long SPREAD_USER = 10001L;
	private static final Long RECEIVED_USER = 10003L;
	private static final String SPREAD_ROOM = "ROOM1";

	private final MoneySpreadRequest request = new MoneySpreadRequest();
	private static SpreadMoney spreadMoney;

	@Autowired
	private MoneyService moneyService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SpreadMoneyRepository spreadMoneyRepository;

	@Before
	public void setUp() {
		request.setMoney(new BigDecimal(50000));
		request.setHeadCount(4L);

		String token = moneyService.spread(SPREAD_USER, SPREAD_ROOM, request);
		spreadMoney = spreadMoneyRepository.findByTokenAndRecvExpDateAfter(token, LocalDateTime.now())
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.ExpireReceiveDate));
	}

	@DisplayName(value = "뿌리기를 생성한 사용자 확인")
	@Test
	public void testCreateUser() {
		User user = userRepository.findById(SPREAD_USER)
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.NotExistUser));
		assertThat(spreadMoney.isSpreadUser(user)).isTrue();
	}

	@Transactional
	@DisplayName(value = "받기 가능한 사용자 수 확인")
	@Test
	public void testRemainedHeadCount() {
		long expected = spreadMoneyRepository.findByTokenAndRecvExpDateAfter(spreadMoney.getToken(), LocalDateTime.now())
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.ExpireReceiveDate))
				.getRemainedHeadCount();

		assertThat(spreadMoney.getHeadCount()).isEqualTo(expected);
	}

	@DisplayName(value = "조회한 뿌리기의 받기 만료 기한이 생성 시간으로부터 10분 이후인지 확인")
	@Test
	public void testReceiveExpireDate() {
		LocalDateTime expected = spreadMoney.getCreatedDate().plusMinutes(10L);

		assertThat(spreadMoney.getRecvExpDate()).isEqualTo(expected);
	}

	@DisplayName(value = "조회한 뿌리기의 조회 만료 기한이 생성 시간으로부터 10분 이후인지 확인")
	@Test
	public void testViewExpireDate() {
		LocalDateTime expected = spreadMoney.getCreatedDate().plusDays(7L);

		assertThat(spreadMoney.getViewExpDate()).isEqualTo(expected);
	}
}

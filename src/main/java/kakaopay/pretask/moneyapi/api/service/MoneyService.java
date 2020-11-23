package kakaopay.pretask.moneyapi.api.service;

import kakaopay.pretask.moneyapi.api.dto.MoneySpreadRequest;
import kakaopay.pretask.moneyapi.api.dto.MoneySpreadViewResponse;
import kakaopay.pretask.moneyapi.api.exception.MoneyErrorCode;
import kakaopay.pretask.moneyapi.api.exception.MoneyException;
import kakaopay.pretask.moneyapi.domain.UserInRoom;
import kakaopay.pretask.moneyapi.domain.UserInRoomRepository;
import kakaopay.pretask.moneyapi.domain.event.*;
import kakaopay.pretask.moneyapi.domain.event.distributing.Distributing;
import kakaopay.pretask.moneyapi.domain.event.distributing.strategy.SameStrategy;
import kakaopay.pretask.moneyapi.domain.room.Room;
import kakaopay.pretask.moneyapi.domain.room.RoomRepository;
import kakaopay.pretask.moneyapi.domain.user.User;
import kakaopay.pretask.moneyapi.domain.user.UserRepository;
import kakaopay.pretask.moneyapi.utils.token.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EnableRetry
@RequiredArgsConstructor
@Service
public class MoneyService {

	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private final UserInRoomRepository userInRoomRepository;

	private final SpreadMoneyRepository spreadMoneyRepository;
	private final ReceivedMoneyRepository receivedMoneyRepository;

	/**
	 * 금액, 인원 받아서 토큰 발급 및 뿌리기 건 생성
	 * @param userId : 사용자 아이디
	 * @param roomId : 대화방 아이디
	 * @param request : 사용자 요청 데이터
	 * @return token
	 */
	@Transactional
	public String spread(Long userId, String roomId, MoneySpreadRequest request) {
		UserInRoom userInRoom = findUserInRoom(userId, roomId);
		validatePossibleHeadCount(userInRoom.getRoom(), request.getHeadCount());
		return saveNewSpreadMoney(userInRoom, request);
	}

	/**
	 * 조건에 맞는 사용자 조회 및 검증 후 받기 처리
	 * (token 일치, 받기 시간 만료 전, 할당 받은 기록이 없고 뿌리기하지 않은 사용자)
	 * @param userId : 사용자 아이디
	 * @param roomId : 대화방 아이디
	 * @param token : 뿌리기 건 토큰값
	 * @return 받은 금액
	 */
	@Transactional
	public BigDecimal receive(Long userId, String roomId, String token) {
		User user = findUserInRoom(userId, roomId).getUser();
		SpreadMoney event = spreadMoneyRepository.findByTokenAndRecvExpDateAfter(token, LocalDateTime.now())
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.ExpireReceiveDate));

		validateReceivingUser(event, user);

		ReceivedMoney receivedMoney = ReceivedMoney.builder()
				.event(event)
				.user(user)
				.money(distributeMoney(event.getRemainedHeadCount(), event.getRemainedMoney()))
				.build();

		receivedMoneyRepository.save(receivedMoney);
		return receivedMoney.getMoney();
	}

	/**
	 * 뿌리기 한 사용자의 뿌리기 건 조회
	 * (token 일치, 조회 시간 만료 전)
	 * @param userId : 사용자 아이디
	 * @param roomId : 대화방 아이디
	 * @param token : 뿌리기 건 토큰값
	 * @return 조회한 뿌리기 건 정보
	 */
	@Transactional(readOnly = true)
	public MoneySpreadViewResponse viewInfo(Long userId, String roomId, String token) {
		User user = findUserInRoom(userId, roomId).getUser();
		SpreadMoney event = spreadMoneyRepository.findByTokenAndViewExpDateAfter(token, LocalDateTime.now())
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.ExpireViewDate));

		validateViewingUser(event, user);
		return new MoneySpreadViewResponse(event);
	}

	/**
	 * 해당 방에서 받기 가능한 최대 인원 수 검증
	 * @param room : 방 정보
	 * @param requestHeadCount : 요청한 인원 수
	 */
	private void validatePossibleHeadCount(Room room, long requestHeadCount) {
		long headCountInRoom = room.getUsers().size();
		if (requestHeadCount > headCountInRoom - 1) {
			throw new MoneyException(MoneyErrorCode.OverHeadCount);
		}
	}

	/**
	 * 요청자의 받기 가능 여부 검증
	 * @param event : 요청 받은 토큰 값으로 조회한 뿌리기
	 * @param user : 요청 사용자
	 */
	private void validateReceivingUser(SpreadMoney event, User user) {
		if (event.isSpreadUser(user)) {
			throw new MoneyException(MoneyErrorCode.SpreadUser);
		}
		if (event.isReceivedUser(user)) {
			throw new MoneyException(MoneyErrorCode.AlreadyReceivedUser);
		}
		if (event.isAllReceived()) {
			throw new MoneyException(MoneyErrorCode.AlreadyReceivedUser);
		}
	}

	/**
	 * 요청자의 조회 가능 여부 검증
	 * @param event : 요청 받은 토큰 값으로 조회한 뿌리기
	 * @param user : 요청 사용자
	 */
	private void validateViewingUser(SpreadMoney event, User user) {
		if (event.isNotSpreadUser(user)) {
			throw new MoneyException(MoneyErrorCode.NotSpreadUser);
		}
	}

	/**
	 * 요청 정보로 뿌리기 이벤트 생성 및 토큰 발급 (토큰 중복 시 1번 재시도)
	 * @param userInRoom : 대화방과 사용자 정보
	 * @param request : 요청 정보
	 * @return 생성 토큰
	 */
	@Retryable(maxAttempts = 2, value = {DataIntegrityViolationException.class, ConstraintViolationException.class})
	public String saveNewSpreadMoney(UserInRoom userInRoom, MoneySpreadRequest request) {
		SpreadMoney event = SpreadMoney.builder()
				.token(TokenUtils.create())
				.user(userInRoom.getUser())
				.room(userInRoom.getRoom())
				.money(request.getMoney())
				.headCount(request.getHeadCount())
				.build();

		spreadMoneyRepository.save(event);
		return event.getToken();
	}

	/**
	 * 디비에 저장되어있는 방 정보 조회
	 * @param roomId : 방 아이디
	 * @return 방 정보
	 */
	private Room findRoom(String roomId) {
		return roomRepository.findByRoomId(roomId)
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.NotExistRoom));
	}

	/**
	 * 디비에 저장되어있는 사용자 정보 조회
	 * @param userId : 사용자 아이디
	 * @return 사용자 정보
	 */
	private User findUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.NotExistUser));
	}

	/**
	 * 디비에 저장되어있는 사용자 + 속해있는 방 정보
	 * @param userId : 사용자 아이디
	 * @param roomId : 방 아이디
	 * @return 사용자 정보 + 속해있는 방 정보
	 */
	private UserInRoom findUserInRoom(Long userId, String roomId) {
		return userInRoomRepository.findByUserAndRoom(findUser(userId), findRoom(roomId))
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.NotExistUserInRoom));
	}

	/**
	 * 머니 분배
	 * @param headCount : 분배할 남은 인원 수
	 * @param money : 분배할 남은 머니
	 * @return 분배한 머니
	 */
	private BigDecimal distributeMoney(Long headCount, BigDecimal money) {
		return new Distributing(new SameStrategy())
				.distribute(headCount, money);
	}
}

package kakaopay.pretask.moneyapi.api.service;

import kakaopay.pretask.moneyapi.api.dto.MoneySpreadRequest;
import kakaopay.pretask.moneyapi.api.dto.MoneySpreadViewResponse;
import kakaopay.pretask.moneyapi.api.exception.MoneyErrorCode;
import kakaopay.pretask.moneyapi.api.exception.MoneyException;
import kakaopay.pretask.moneyapi.domain.UsersInRoom;
import kakaopay.pretask.moneyapi.domain.UsersInRoomRepository;
import kakaopay.pretask.moneyapi.domain.event.*;
import kakaopay.pretask.moneyapi.domain.event.distributing.Distributing;
import kakaopay.pretask.moneyapi.domain.event.distributing.strategy.SameStrategy;
import kakaopay.pretask.moneyapi.domain.room.Room;
import kakaopay.pretask.moneyapi.domain.room.RoomRepository;
import kakaopay.pretask.moneyapi.domain.user.User;
import kakaopay.pretask.moneyapi.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MoneyService {

	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private final UsersInRoomRepository usersInRoomRepository;

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
		UsersInRoom usersInRoom = findUserInRoom(userId, roomId);

		validatePossibleHeadCount(usersInRoom.getRoom(), request.getHeadCount());

		SpreadMoney event = SpreadMoney.builder()
				.user(usersInRoom.getUser())
				.room(usersInRoom.getRoom())
				.money(request.getMoney())
				.headCount(request.getHeadCount())
				.build();

		spreadMoneyRepository.save(event);
		return event.getToken();
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

	private void validatePossibleHeadCount(Room room, long requestHeadCount) {
		long headCountInRoom = room.getUsers().size();
		if (requestHeadCount > headCountInRoom - 1) {
			throw new MoneyException(MoneyErrorCode.OverHeadCount);
		}
	}

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

	private void validateViewingUser(SpreadMoney event, User user) {
		if (event.isNotSpreadUser(user)) {
			throw new MoneyException(MoneyErrorCode.NotSpreadUser);
		}
	}

	@Transactional(readOnly = true)
	protected Room findRoom(String roomId) {
		return roomRepository.findByRoomId(roomId)
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.MisMatchRoomId));
	}

	@Transactional(readOnly = true)
	protected User findUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.MisMatchUserId));
	}

	@Transactional(readOnly = true)
	protected UsersInRoom findUserInRoom(Long userId, String roomId) {
		return usersInRoomRepository.findByUserAndRoom(findUser(userId), findRoom(roomId))
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.MisMatchUserInRoom));
	}

	private BigDecimal distributeMoney(Long headCount, BigDecimal money) {
		Distributing method = new Distributing();
		method.setStrategy(new SameStrategy());
		return method.distribute(headCount, money);
	}

}

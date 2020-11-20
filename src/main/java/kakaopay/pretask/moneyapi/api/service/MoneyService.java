package kakaopay.pretask.moneyapi.api.service;

import kakaopay.pretask.moneyapi.api.dto.MoneySpreadRequest;
import kakaopay.pretask.moneyapi.api.dto.MoneySpreadViewResponse;
import kakaopay.pretask.moneyapi.api.exception.MoneyErrorCode;
import kakaopay.pretask.moneyapi.api.exception.MoneyException;
import kakaopay.pretask.moneyapi.domain.UsersInRoomRepository;
import kakaopay.pretask.moneyapi.domain.event.MoneyEvent;
import kakaopay.pretask.moneyapi.domain.event.MoneyEventRepository;
import kakaopay.pretask.moneyapi.domain.event.SubMoneyEvent;
import kakaopay.pretask.moneyapi.domain.event.SubMoneyEventRepository;
import kakaopay.pretask.moneyapi.domain.room.Room;
import kakaopay.pretask.moneyapi.domain.room.RoomRepository;
import kakaopay.pretask.moneyapi.domain.user.User;
import kakaopay.pretask.moneyapi.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MoneyService {

	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private final UsersInRoomRepository usersInRoomRepository;

	private final MoneyEventRepository moneyEventRepository;
	private final SubMoneyEventRepository subMoneyEventRepository;

	/**
	 * 금액, 인원 받아서 총 금액을 인원 수에 맞게 분배 처리하고 토큰 발급
	 * @param userId : 사용자 아이디
	 * @param roomId : 대화방 아이디
	 * @param request : 사용자 요청 데이터
	 * @return token
	 */
	@Transactional
	public String spread(Long userId, String roomId, MoneySpreadRequest request) {
		Room room = roomRepository.findByRoomId(roomId)
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.MisMatchRoomId));
		User host = userRepository.findById(userId)
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.MisMatchUserId));

		long headCount = usersInRoomRepository.countUsersInRoomByRoomId(roomId);
		if (request.getHeadCount() > headCount - 1) {
			throw new MoneyException(MoneyErrorCode.OverHeadCount);
		}

		MoneyEvent event = MoneyEvent.builder()
				.user(host)
				.room(room)
				.money(request.getMoney())
				.build();
		moneyEventRepository.save(event);
		subMoneyEventRepository.saveAll(event.distributeMoney(request.getHeadCount()));
		return event.getToken();
	}

	/**
	 * 조건에 맞는 사용자 조회 및 받기 1건 할당
	 * (token 일치, 받기 시간 만료 전, 할당 받은 기록이 없고 뿌리기하지 않은 사용자)
	 * @param userId : 사용자 아이디
	 * @param roomId : 대화방 아이디
	 * @param token : 뿌리기 건 토큰값
	 * @return 받은 금액
	 */
	@Transactional
	public long receive(Long userId, String roomId, String token) {
		Room room = roomRepository.findByRoomId(roomId)
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.MisMatchRoomId));
		User user = usersInRoomRepository.findByUser_UserIdAndRoom_RoomId(userId, roomId)
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.MisMatchUserId))
				.getUser();

		MoneyEvent event = moneyEventRepository.findByTokenAndRoomIsAndRecvExpDateAfter(token, room, LocalDateTime.now())
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.ExpireReceiveDate));

		long userCount = subMoneyEventRepository.countSubMoneyEventByUser_UserIdOrEvent_User_UserId(userId, userId);
		if (userCount > 0) {
			throw new MoneyException(MoneyErrorCode.AlreadyReceivedUser);
		}

		SubMoneyEvent subEvent = subMoneyEventRepository.findFirstByAssignedYnAndEvent_Token('N', token);
		subEvent.update(user);
		return subEvent.getMoney();
	}

	/**
	 * 뿌리기 한 사용자의 뿌리기 건 조회
	 * (token 일치, 조회 시간 만료 전)
	 * @param userId : 사용자 아이디
	 * @param roomId : 대화방 아이디
	 * @param token : 뿌리기 건 토큰값
	 * @return 조회한 뿌리기 건 정보
	 */
	@Transactional
	public MoneySpreadViewResponse viewInfo(Long userId, String roomId, String token) {
		Room room = roomRepository.findByRoomId(roomId)
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.MisMatchRoomId));
		MoneyEvent event = moneyEventRepository.findByTokenAndUser_UserIdAndViewExpDateAfter(token, userId, LocalDateTime.now())
				.orElseThrow(() -> new MoneyException(MoneyErrorCode.ExpireViewDateOrNotAuthUser));

		List<SubMoneyEvent> subEvents = subMoneyEventRepository.findAllByAssignedYnAndEvent_Token('Y', token);
		long receivedMoney = subEvents.stream()
				.mapToLong(SubMoneyEvent::getMoney)
				.sum();

		if (receivedMoney == event.getMoney()) {
			event.updateAllRecvY();
		}

		return new MoneySpreadViewResponse(event, receivedMoney);
	}
}

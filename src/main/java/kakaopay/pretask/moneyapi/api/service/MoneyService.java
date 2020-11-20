package kakaopay.pretask.moneyapi.api.service;

import kakaopay.pretask.moneyapi.api.dto.MoneySpreadRequest;
import kakaopay.pretask.moneyapi.api.dto.MoneySpreadResponse;
import kakaopay.pretask.moneyapi.api.dto.MoneySpreadViewResponse;
import kakaopay.pretask.moneyapi.domain.UsersInRoom;
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
	 * @param userId
	 * @param roomId
	 * @param request
	 * @return token
	 */
	@Transactional
	public String spread(Long userId, String roomId, MoneySpreadRequest request) {
		// TODO Illegal 말고 Exception 추가
		Room room = roomRepository.findByRoomId(roomId)
				.orElseThrow(() -> new IllegalArgumentException("해당 방 번호와 일치하는 대화방이 없습니다."));
		User host = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("해당 아이디와 일치하는 사용자가 없습니다."));

		long headCount = usersInRoomRepository.countUsersInRoomByRoomId(roomId);
		if (request.getHeadCount() > headCount - 1) {
			throw new IllegalArgumentException("받을 인원은 대화방 인원 수를 초과할 수 없습니다.");
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
	 * @param userId
	 * @param roomId
	 * @param token
	 * @return
	 */
	@Transactional
	public long receive(Long userId, String roomId, String token) {
		Room room = roomRepository.findByRoomId(roomId)
				.orElseThrow(() -> new IllegalArgumentException("해당 방 번호와 일치하는 대화방이 없습니다."));
		User user = usersInRoomRepository.findByUser_UserIdAndRoom_RoomId(userId, roomId)
				.orElseThrow(() -> new IllegalArgumentException("대화방에 포함되지 않은 사용자입니다."))
				.getUser();

		MoneyEvent event = moneyEventRepository.findByTokenAndRoomIsAndRecvExpDateAfter(token, room, LocalDateTime.now())
				.orElseThrow(() -> new IllegalArgumentException("요청하신 뿌리기 건의 받기 기한이 만료되었습니다."));

		long userCount = subMoneyEventRepository.countSubMoneyEventByUser_UserIdOrEvent_User_UserId(userId, userId);
		if (userCount > 0) {
			throw new IllegalArgumentException("뿌리기를 했거나 이미 받기 완료된 사용자입니다.");
		}

		SubMoneyEvent subEvent = subMoneyEventRepository.findFirstByAssignedYnAndEvent_Token('N', token);
		subEvent.update(user);
		return subEvent.getMoney();
	}

	/**
	 * 뿌리기 한 사용자의 뿌리기 건 조회
	 * (token 일치, 조회 시간 만료 전)
	 * @param userId
	 * @param roomId
	 * @param token
	 * @return
	 */
	@Transactional
	public MoneyEvent viewInfo(Long userId, String roomId, String token) {
		Room room = roomRepository.findByRoomId(roomId)
				.orElseThrow(() -> new IllegalArgumentException("해당 방 번호와 일치하는 대화방이 없습니다."));
		return moneyEventRepository.findByTokenAndUser_UserIdAndViewExpDateAfter(token, userId, LocalDateTime.now())
				.orElseThrow(() -> new IllegalArgumentException("요청하신 뿌리기 건의 조회 기한이 만료되었거나 조회 권한이 없는 사용자입니다."));
	}
}

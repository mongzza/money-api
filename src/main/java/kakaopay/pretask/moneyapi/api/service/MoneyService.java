package kakaopay.pretask.moneyapi.api.service;

import kakaopay.pretask.moneyapi.api.dto.MoneySpreadRequest;
import kakaopay.pretask.moneyapi.domain.UsersInRoomRepository;
import kakaopay.pretask.moneyapi.domain.event.MoneyEvent;
import kakaopay.pretask.moneyapi.domain.event.MoneyEventRepository;
import kakaopay.pretask.moneyapi.domain.event.SubMoneyEventRepository;
import kakaopay.pretask.moneyapi.domain.room.Room;
import kakaopay.pretask.moneyapi.domain.room.RoomRepository;
import kakaopay.pretask.moneyapi.domain.user.User;
import kakaopay.pretask.moneyapi.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
	public String spread(Long userId, String roomId, MoneySpreadRequest request) {
		Room room = roomRepository.findByRoomId(roomId)
				.orElseThrow(() -> new IllegalArgumentException("해당 방 번호와 일치하는 대화방이 없습니다."));
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("해당 아이디와 일치하는 사용자가 없습니다."));

		Long headCount = usersInRoomRepository.countUsersInRoomByRoomId(roomId);
		if (request.getHeadCount() > headCount - 1) {
			throw new IllegalArgumentException("받을 인원은 대화방 인원 수를 초과할 수 없습니다.");
		}

		MoneyEvent event = MoneyEvent.builder()
				.user(user)
				.room(room)
				.money(request.getMoney())
				.build();

		subMoneyEventRepository.saveAll(event.distributeMoney(request.getHeadCount()));
		return event.getToken();
	}


	// 2.
	// token 일치하고, 받기 만료 전이고, 할당되지 않은 sub_event 한 개에 유저아이디, 할당 여부 업데이트
	// (유저 아이디는 헤더에서 받기 가능)
	// 유저 아이디가 뿌리기한 아이디면 실패 응답
	// 유저 아이디가 이미 할당 받은 아이디면 실패 응답
	// 해당 건 금액 조회해서 반환 처리 메소드(Long)

	// 3.
	// token 일치하고, 조회 만료 전이고, 유저 아이디가 뿌리기한 아이디이면 뿌리기 정보 조회
	// moneyevent, subevent 조인
	// (List<MoneySpreadViewResponse>)
}

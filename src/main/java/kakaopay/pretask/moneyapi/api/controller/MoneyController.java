package kakaopay.pretask.moneyapi.api.controller;

import kakaopay.pretask.moneyapi.api.dto.*;
import kakaopay.pretask.moneyapi.api.service.MoneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/money", produces = "application/json")
public class MoneyController {

	private static final String USER_ID_HEADER = "X-USER-ID";
	private static final String ROOM_ID_HEADER = "X-ROOM-ID";

	private final MoneyService moneyService;

	/**
	 *
	 * @param userId : 요청자 ID
	 * @param roomId : 요청자의 대화방 ID
	 * @param request : 요청 바디(인원 수, 금액)
	 * @return 발급한 뿌리기 토큰
	 */
	@PostMapping(value = "/spread")
	public MoneySpreadResponse spreadMoney(@RequestHeader(USER_ID_HEADER) Long userId,
	                                       @RequestHeader(ROOM_ID_HEADER) String roomId,
	                                       @RequestBody MoneySpreadRequest request) {

		return new MoneySpreadResponse(moneyService.spread(userId, roomId, request));
	}

	/**
	 *
	 * @param userId : 요청자 ID
	 * @param roomId : 요청자의 대화방 ID
	 * @param token : 뿌리기 토큰
	 * @return 분배 금액
	 */
	@PutMapping(value = "/spread/{token}")
	public MoneyRecvResponse receiveMoney(@RequestHeader(USER_ID_HEADER) Long userId,
	                                      @RequestHeader(ROOM_ID_HEADER) String roomId,
	                                      @PathVariable String token) {

		return new MoneyRecvResponse(moneyService.receive(userId, roomId, token));
	}

	/**
	 * 조회 API
	 * @param userId : 요청자 ID
	 * @param roomId : 요청자의 대화방 ID
	 * @param token : 뿌리기 토큰
	 * @return 뿌린시간, 뿌린금액, 받기 완료 금액, 받기 완료 정보([ 받은 금액, 받은 사람 아이디 ] 리스트)
	 */
	@GetMapping(value = "/spread/{token}")
	public MoneySpreadViewResponse viewSpreadInfo(@RequestHeader(USER_ID_HEADER) Long userId,
	                                              @RequestHeader(ROOM_ID_HEADER) String roomId,
	                                              @PathVariable String token) {

		return moneyService.viewInfo(userId, roomId, token);
	}

}

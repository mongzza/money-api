package kakaopay.pretask.moneyapi.api.controller;

import kakaopay.pretask.moneyapi.api.dto.*;
import kakaopay.pretask.moneyapi.api.service.MoneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/money", produces = "application/json")
public class MoneyController {

	private static final String USER_ID_HEADER = "X-USER-ID";
	private static final String ROOM_ID_HEADER = "X-ROOM-ID";

	private final MoneyService moneyService;

	/**
	 * 뿌리기 API
	 * @param headers : 요청 헤더
	 * @param request : 요청 바디 (인원 수, 금액)
	 * @return 발급된 토큰
	 */
	@PostMapping(value = "/spread")
	public MoneySpreadResponse spreadMoney(@RequestHeader HttpHeaders headers, @RequestBody MoneySpreadRequest request) {
		long userId = Long.parseLong(Objects.requireNonNull(headers.getFirst(USER_ID_HEADER)));
		String roomId = headers.getFirst(ROOM_ID_HEADER);

		return new MoneySpreadResponse(moneyService.spread(userId, roomId, request));
	}

	/**
	 * 받기 API
	 * @param headers : 요청 헤더
	 * @param token : 뿌리기 건 토큰값
	 * @return 분배 금액
	 */
	@PutMapping(value = "/spread/{token}")
	public MoneyRecvResponse receiveMoney(@RequestHeader HttpHeaders headers, @PathVariable String token) {
		long userId = Long.parseLong(Objects.requireNonNull(headers.getFirst(USER_ID_HEADER)));
		String roomId = headers.getFirst(ROOM_ID_HEADER);

		return new MoneyRecvResponse(moneyService.receive(userId, roomId, token));
	}

	/**
	 * 조회 API
	 * @param headers : 요청 헤더
	 * @param token : 뿌리기 건 토큰
	 * @return 뿌린시간, 뿌린금액, 받기 완료 금액, 받기 완료 정보([ 받은 금액, 받은 사람 아이디 ] 리스트)
	 */
	@GetMapping(value = "/spread/{token}")
	public MoneySpreadViewResponse viewSpreadInfo(@RequestHeader HttpHeaders headers, @PathVariable String token) {
		long userId = Long.parseLong(Objects.requireNonNull(headers.getFirst(USER_ID_HEADER)));
		String roomId = headers.getFirst(ROOM_ID_HEADER);

		return moneyService.viewInfo(userId, roomId, token);
	}

}

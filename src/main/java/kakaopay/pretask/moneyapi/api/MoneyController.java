package kakaopay.pretask.moneyapi.api;

import kakaopay.pretask.moneyapi.api.dto.MoneyRecvResponse;
import kakaopay.pretask.moneyapi.api.dto.MoneySpreadRequest;
import kakaopay.pretask.moneyapi.api.dto.MoneySpreadResponse;
import kakaopay.pretask.moneyapi.api.dto.MoneySpreadViewResponse;
import kakaopay.pretask.moneyapi.api.service.MoneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController(value = "/money")
public class MoneyController {
	private static final String USER_ID_HEADER = "X-USER-ID";
	private static final String ROOM_ID_HEADER = "X-ROOM-ID";

	private final MoneyService moneyService;

	// headers 어케 처리할지 고민 필요, 중복 코드
	// token을 url에 노출해도 되는지 확인 필요 => ok면 MoneyRecvReq, res, MoneyViewReq, res 불필요 no면 dto 사용해서 데이터 전달

	/**
	 * 뿌리기 API
	 * @param headers
	 * @param request
	 * @return 발급된 토큰
	 */
	@PostMapping(value = "/spread")
	public MoneySpreadResponse spreadMoney(@RequestHeader HttpHeaders headers, @RequestBody MoneySpreadRequest request) {
		MoneySpreadResponse response = new MoneySpreadResponse();

		try {
			Long userId = Long.parseLong(Objects.requireNonNull(headers.getFirst(USER_ID_HEADER)));
			String roomId = headers.getFirst(ROOM_ID_HEADER);

			response.setToken(moneyService.spread(userId, roomId, request));
			response.setCode(2000);
			response.setMessage("SUCCESS");
		} catch(IllegalArgumentException e) {
			// exception 만들어서 new Exception()??
			response.setCode(4004);
			response.setMessage(e.getMessage());
		}

		return response;
	}

	/**
	 * 받기 API
	 * @param headers
	 * @param token
	 * @return 분배 금액
	 */
	@PutMapping(value = "/spread/{token}")
	public Long receiveMoney(@RequestHeader HttpHeaders headers, @PathVariable String token) {
		String userId = headers.getFirst(USER_ID_HEADER);
		String roomId = headers.getFirst(ROOM_ID_HEADER);
		return null;
		//return moneyService.receive(userId, roomId, token);
	}

	/**
	 * 조회 API
	 * @param headers
	 * @param token
	 * @return 뿌린시간, 뿌린금액, 받기 완료 금액, 받기 완료 정보([ 받은 금액, 받은 사람 아이디 ] 리스트)
	 */
	@GetMapping(value = "/spread/{token}")
	public MoneySpreadViewResponse viewSpreadInfo(@RequestHeader HttpHeaders headers, @PathVariable String token) {
		String userId = headers.getFirst(USER_ID_HEADER);
		String roomId = headers.getFirst(ROOM_ID_HEADER);
		return null;
		//return moneyService.viewInfo(userId, roomId, token);
	}
}

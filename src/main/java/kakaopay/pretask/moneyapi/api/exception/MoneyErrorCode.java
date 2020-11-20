package kakaopay.pretask.moneyapi.api.exception;

import lombok.Getter;

@Getter
public enum MoneyErrorCode {
	MisMatchRoomId(4000, "존재하지 않는 대화방입니다."),
	MisMatchUserId(4001, "대화방에 존재하지 않는 사용자입니다."),

	OverHeadCount(4002, "받을 인원은 대화방 인원 수를 초과할 수 없습니다."),
	AlreadyReceivedUser(4003, "뿌리기를 했거나 이미 받기 완료된 사용자입니다."),

	ExpireReceiveDate(4004, "요청하신 뿌리기 건의 받기 기한이 만료되었습니다."),
	ExpireViewDateOrNotAuthUser(4005, "요청하신 뿌리기 건의 조회 기한이 만료되었거나 조회 권한이 없는 사용자입니다.")
	;

	private final int code;
	private final String message;

	MoneyErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

}

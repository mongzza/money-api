package kakaopay.pretask.moneyapi.api.exception;

import lombok.Getter;

@Getter
public enum MoneyErrorCode {
	MisMatchRoomId(4000, "존재하지 않는 대화방입니다."),
	MisMatchUserId(4001, "존재하지 않는 사용자입니다."),
	MisMatchUserInRoom(4002, "대화방에 존재하지 않는 사용자입니다."),

	OverHeadCount(4003, "받을 인원은 본인을 제외한 대화방 인원 수를 초과할 수 없습니다."),
	SpreadUser(4004, "뿌리기한 사용자는 머니를 받을 수 없습니다."),
	AlreadyReceivedUser(4005, "머니를 이미 받은 사용자입니다."),
	AlreadyAllReceived(4006, "받기 가능 제한 인원이 초과되었습니다."),

	ExpireReceiveDate(4007, "요청하신 뿌리기 건의 받기 기한이 만료되었습니다."),
	ExpireViewDate(4008, "요청하신 뿌리기 건의 조회 기한이 만료되었습니다."),

	NotSpreadUser(4009, "뿌리기한 사용자만 조회 가능합니다.")
	;

	private final int code;
	private final String message;

	MoneyErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

}

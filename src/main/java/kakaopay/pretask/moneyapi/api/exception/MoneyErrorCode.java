package kakaopay.pretask.moneyapi.api.exception;

import lombok.Getter;

@Getter
public enum MoneyErrorCode {
	NotExistRoom(4000, "존재하지 않는 대화방입니다."),
	NotExistUser(4001, "존재하지 않는 사용자입니다."),
	NotExistUserInRoom(4002, "대화방에 존재하지 않는 사용자입니다."),
	OverHeadCount(4003, "받을 인원은 본인을 제외한 대화방 인원 수를 초과할 수 없습니다."),
	MinimumMoney(4004, "뿌릴 금액은 최소 1000원부터 가능합니다."),

	SpreadUser(4005, "뿌리기한 사용자는 머니를 받을 수 없습니다."),
	AlreadyReceivedUser(4006, "이미 머니를 받은 사용자입니다."),
	AlreadyAllReceived(4007, "받기 가능 제한 인원이 초과되었습니다."),
	ExpireReceiveDate(4008, "요청하신 뿌리기 건의 받기 기한이 만료되었습니다."),

	ExpireViewDate(4009, "요청하신 뿌리기 건의 조회 기한이 만료되었습니다."),
	NotSpreadUser(4010, "뿌리기한 사용자만 조회 가능합니다."),

	MissingHeader(4011, "필수 데이터가 누락되었습니다."),

	InternalError(5000, "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
	;

	private final int code;
	private final String message;

	MoneyErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

}

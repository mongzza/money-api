package kakaopay.pretask.moneyapi.api.exception;

public class MoneyException extends RuntimeException {
	private static final long serialVersionUID = 7309394879222684193L;

	private final MoneyErrorCode code;

	public MoneyException(MoneyErrorCode code) {
		this.code = code;
	}

	public MoneyErrorCode getCode() {
		return this.code;
	}
}

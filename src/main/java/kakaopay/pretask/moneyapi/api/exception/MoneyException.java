package kakaopay.pretask.moneyapi.api.exception;

public class MoneyException extends RuntimeException {

	private static final long serialVersionUID = -2131920218409172849L;

	private final MoneyErrorCode code;
	private final String message;

	public MoneyException(MoneyErrorCode code) {
		this.code = code;
		this.message = code.getMessage();
	}

	public MoneyErrorCode getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}
}

package kakaopay.pretask.moneyapi.interceptor;

import kakaopay.pretask.moneyapi.api.exception.MoneyErrorCode;
import kakaopay.pretask.moneyapi.api.exception.MoneyException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HeaderInterceptor extends HandlerInterceptorAdapter {

	private static final String USER_ID_HEADER = "X-USER-ID";
	private static final String ROOM_ID_HEADER = "X-ROOM-ID";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String userId = request.getHeader(USER_ID_HEADER);
		String roomId = request.getHeader(ROOM_ID_HEADER);

		if (isEmpty(userId) || isEmpty(roomId)) {
			throw new MoneyException(MoneyErrorCode.MissingHeader);
		}
		return true;
	}

	private boolean isEmpty(String string) {
		return null == string || "".equals(string);
	}
}

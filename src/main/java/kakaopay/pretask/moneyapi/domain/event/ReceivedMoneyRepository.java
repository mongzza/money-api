package kakaopay.pretask.moneyapi.domain.event;

import kakaopay.pretask.moneyapi.domain.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReceivedMoneyRepository extends CrudRepository<ReceivedMoney, Long> {

	boolean existsByUser_UserId(Long userId);

	List<ReceivedMoney> findAllByEvent_Token(String token);
}

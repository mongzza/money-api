package kakaopay.pretask.moneyapi.domain.event;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SpreadMoneyRepository extends CrudRepository<SpreadMoney, Long> {

	Optional<SpreadMoney> findByTokenAndRecvExpDateAfter(String token, LocalDateTime now);

	boolean existsByTokenAndUser_UserId(String token, Long userId);

	Optional<SpreadMoney> findByTokenAndViewExpDateAfter(String token, LocalDateTime now);
}
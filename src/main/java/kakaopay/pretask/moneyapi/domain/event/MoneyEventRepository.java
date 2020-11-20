package kakaopay.pretask.moneyapi.domain.event;

import kakaopay.pretask.moneyapi.domain.room.Room;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MoneyEventRepository extends CrudRepository<MoneyEvent, Long> {

	Optional<MoneyEvent> findByTokenAndRoomIsAndRecvExpDateAfter(String token, Room room, LocalDateTime now);

	Optional<MoneyEvent> findByTokenAndUser_UserIdAndViewExpDateAfter(String token, Long userId, LocalDateTime now);
}

package kakaopay.pretask.moneyapi.domain;

import kakaopay.pretask.moneyapi.domain.room.Room;
import kakaopay.pretask.moneyapi.domain.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserInRoomRepository extends CrudRepository<UserInRoom, Long> {

	Optional<UserInRoom> findByUserAndRoom(User user, Room room);

}

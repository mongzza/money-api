package kakaopay.pretask.moneyapi.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsersInRoomRepository extends CrudRepository<UsersInRoom, Long> {

	@Query("SELECT count(user.userId) FROM UsersInRoom WHERE room.roomId = ?1")
	Long countUsersInRoomByRoomId(String roomId);
}

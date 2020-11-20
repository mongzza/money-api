package kakaopay.pretask.moneyapi.domain;

import kakaopay.pretask.moneyapi.domain.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersInRoomRepository extends CrudRepository<UsersInRoom, Long> {

	@Query("SELECT count(user.userId) FROM UsersInRoom WHERE room.roomId = ?1")
	long countUsersInRoomByRoomId(String roomId);

	Optional<UsersInRoom> findByUser_UserIdAndRoom_RoomId(Long userId, String roomId);
}

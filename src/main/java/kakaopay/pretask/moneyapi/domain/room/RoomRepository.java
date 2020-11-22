package kakaopay.pretask.moneyapi.domain.room;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RoomRepository extends CrudRepository<Room, Long> {

	Optional<Room> findByRoomId(String roomId);

}

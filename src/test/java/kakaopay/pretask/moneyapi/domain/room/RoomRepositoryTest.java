package kakaopay.pretask.moneyapi.domain.room;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoomRepositoryTest {

	@Autowired
	RoomRepository roomRepository;

	@DisplayName(value = "DB에서 조회한 대화방 정보 확인")
	@Test
	public void testFindByRoomId() {
		String roomId = "ROOM1";
		assertThat(roomRepository.findByRoomId(roomId).get().getRoomId())
				.isEqualTo(roomId);
	}
}

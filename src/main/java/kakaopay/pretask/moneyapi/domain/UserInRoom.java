package kakaopay.pretask.moneyapi.domain;

import kakaopay.pretask.moneyapi.domain.room.Room;
import kakaopay.pretask.moneyapi.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class UserInRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "ROOM_ID", referencedColumnName = "ROOM_ID", nullable = false)
	private Room room;
}

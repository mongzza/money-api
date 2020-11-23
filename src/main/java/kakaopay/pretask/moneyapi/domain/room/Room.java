package kakaopay.pretask.moneyapi.domain.room;

import kakaopay.pretask.moneyapi.domain.UserInRoom;
import kakaopay.pretask.moneyapi.domain.event.SpreadMoney;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ROOM_ID", length = 64, nullable = false, unique = true)
	private String roomId;

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private Set<UserInRoom> users = new HashSet<>();

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private Set<SpreadMoney> events = new HashSet<>();

	@Builder
	public Room(String roomId) {
		this.roomId = roomId;
	}
}

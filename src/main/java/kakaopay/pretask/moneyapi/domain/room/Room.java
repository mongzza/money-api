package kakaopay.pretask.moneyapi.domain.room;

import kakaopay.pretask.moneyapi.domain.UserInRoom;
import kakaopay.pretask.moneyapi.domain.event.SpreadMoney;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Room implements Serializable {

	private static final long serialVersionUID = -4026831997462043596L;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Room room = (Room) o;

		return id != null ? id.equals(room.id) : room.id == null;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}

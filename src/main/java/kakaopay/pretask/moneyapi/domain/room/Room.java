package kakaopay.pretask.moneyapi.domain.room;

import kakaopay.pretask.moneyapi.domain.UserInRoom;
import kakaopay.pretask.moneyapi.domain.event.SpreadMoney;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Room implements Serializable {

	private static final long serialVersionUID = -1086290499515965067L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ROOM_ID", length = 64, nullable = false, unique = true)
	private String roomId;

	@OneToMany(mappedBy = "room")
	private List<UserInRoom> users = new ArrayList<>();

	@OneToMany(mappedBy = "room")
	private List<SpreadMoney> events = new ArrayList<>();

	@Builder
	public Room(String roomId) {
		this.roomId = roomId;
	}
}

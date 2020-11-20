package kakaopay.pretask.moneyapi.domain.user;

import kakaopay.pretask.moneyapi.domain.UsersInRoom;
import kakaopay.pretask.moneyapi.domain.event.MoneyEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User implements Serializable {

	private static final long serialVersionUID = -4316778035761407241L;

	@Id
	@Column(name = "USER_ID", length = 64)
	private Long userId;

	@OneToMany(mappedBy = "user")
	private List<UsersInRoom> rooms = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<MoneyEvent> events = new ArrayList<>();
}

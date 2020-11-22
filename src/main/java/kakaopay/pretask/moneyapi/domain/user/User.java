package kakaopay.pretask.moneyapi.domain.user;

import kakaopay.pretask.moneyapi.domain.UsersInRoom;
import kakaopay.pretask.moneyapi.domain.event.SpreadMoney;
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
	private List<SpreadMoney> events = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		return userId.equals(user.userId);
	}

	@Override
	public int hashCode() {
		return userId.hashCode();
	}
}

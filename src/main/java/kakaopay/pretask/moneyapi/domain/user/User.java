package kakaopay.pretask.moneyapi.domain.user;

import kakaopay.pretask.moneyapi.domain.UserInRoom;
import kakaopay.pretask.moneyapi.domain.event.SpreadMoney;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class User {

	@Id
	@Column(name = "USER_ID")
	private Long userId;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<UserInRoom> rooms = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<SpreadMoney> events = new HashSet<>();

	@Builder
	public User(Long userId) {
		this.userId = userId;
	}

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

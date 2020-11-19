package kakaopay.pretask.moneyapi.domain.event;

import kakaopay.pretask.moneyapi.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
public class SubMoneyEvent implements Serializable {

	private static final long serialVersionUID = -7307890925289366866L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "TOKEN", referencedColumnName = "TOKEN")
	private MoneyEvent event;

	@Column(nullable = false)
	private Long money;

	@Column(nullable = false, length = 1)
	private String assignedYn;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@Builder
	public SubMoneyEvent(MoneyEvent event, Long money, User user) {
		this.event = event;
		this.money = money;
		this.user = user;
	}
}

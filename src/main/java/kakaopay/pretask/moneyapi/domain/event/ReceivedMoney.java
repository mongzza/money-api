package kakaopay.pretask.moneyapi.domain.event;

import kakaopay.pretask.moneyapi.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
public class ReceivedMoney implements Serializable {

	private static final long serialVersionUID = -3987264976435657246L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "TOKEN", referencedColumnName = "TOKEN", nullable = false)
	private SpreadMoney event;

	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
	private User user;

	@Column(nullable = false)
	private BigDecimal money;

	@Builder
	public ReceivedMoney(SpreadMoney event, User user, BigDecimal money) {
		this.event = event;
		this.user = user;
		this.money = money;
	}

	public boolean isReceivedUser(User user) {
		return this.getUser().equals(user);
	}

}

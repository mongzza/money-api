package kakaopay.pretask.moneyapi.domain.event;

import kakaopay.pretask.moneyapi.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

// TODO userId null로 나오는 부분, YN값 default 적용 Builder에서 하는게 맞는건지 확인 필요
@Getter
@NoArgsConstructor
@Entity
public class SubMoneyEvent implements Serializable {

	private static final long serialVersionUID = -7307890925289366866L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "TOKEN", referencedColumnName = "TOKEN", nullable = false)
	private MoneyEvent event;

	@Column(nullable = false)
	private Long money;

	@Column(name = "ASSIGNED_YN", nullable = false)
	@ColumnDefault("'N'")
	private char assignedYn;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@Builder
	public SubMoneyEvent(MoneyEvent event, Long money) {
		this.event = event;
		this.money = money;
		this.assignedYn = 'N';
	}

	public void update(User user) {
		this.assignedYn = 'Y';
		this.user = user;
	}
}

package kakaopay.pretask.moneyapi.domain.event;

import kakaopay.pretask.moneyapi.domain.event.distributing.Distributing;
import kakaopay.pretask.moneyapi.domain.event.distributing.strategy.RandomStrategy;
import kakaopay.pretask.moneyapi.domain.room.Room;
import kakaopay.pretask.moneyapi.domain.user.User;
import kakaopay.pretask.moneyapi.utils.TokenUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

// token unique 적용 안되는거, TODO YN값 default 적용 Builder에서 하는게 맞는건지 확인
// https://stackoverflow.com/questions/3126769/uniqueconstraint-annotation-in-java
@Getter
@NoArgsConstructor
@Entity
public class MoneyEvent implements Serializable {

	private static final long serialVersionUID = 7196237196799729882L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "TOKEN", length = 3, unique = true, nullable = false)
	private String token;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@ManyToOne
	@JoinColumn(name = "ROOM_ID", referencedColumnName = "ROOM_ID")
	private Room room;

	@Column(nullable = false)
	private Long money;

	@Column(name = "ALL_RECV_YN", nullable = false)
	@ColumnDefault("'N'")
	private char allRecvYn;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdDate;

	@Column(nullable = false)
	private LocalDateTime recvExpDate;

	@Column(nullable = false)
	private LocalDateTime viewExpDate;

	@OneToMany(mappedBy = "event")
	private List<SubMoneyEvent> subEvents = new ArrayList<>();

	@Builder
	public MoneyEvent(User user, Room room, Long money) {
		this.token = TokenUtils.randomToken();
		this.user = user;
		this.room = room;
		this.money = money;
		this.allRecvYn = 'N';
		this.createdDate = LocalDateTime.now();
		this.recvExpDate = this.createdDate.plusMinutes(10);
		this.viewExpDate = this.createdDate.plusDays(7);
	}

	public List<SubMoneyEvent> distributeMoney(int headCount) {
		List<SubMoneyEvent> subEvents = new ArrayList<>();

		Distributing spreadMoney = new Distributing();
		spreadMoney.setStrategy(new RandomStrategy());
		for (long distributedMoney : spreadMoney.distribute(headCount, this.money)) {
			subEvents.add(SubMoneyEvent.builder()
					.event(this)
					.money(distributedMoney)
					.build());
		}
		return subEvents;
	}

	public void updateAllRecvY() {
		this.allRecvYn = 'Y';
	}

}

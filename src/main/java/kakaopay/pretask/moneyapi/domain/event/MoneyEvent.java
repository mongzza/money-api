package kakaopay.pretask.moneyapi.domain.event;

import kakaopay.pretask.moneyapi.domain.room.Room;
import kakaopay.pretask.moneyapi.domain.user.User;
import kakaopay.pretask.moneyapi.utils.TokenUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class MoneyEvent implements Serializable {

	private static final long serialVersionUID = 7196237196799729882L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "TOKEN", length = 3, nullable = false, unique = true)
	private String token;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@ManyToOne
	@JoinColumn(name = "ROOM_ID", referencedColumnName = "ROOM_ID")
	private Room room;

	@Column(nullable = false)
	private Long money;

	@Column(nullable = false)
	private char allRecvYn;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdDate;

	@Column(nullable = false)
	private LocalDateTime recvExpDate;

	@Column(nullable = false)
	private LocalDateTime viewExpDate;

	@OneToMany(mappedBy = "event")
	private List<SubMoneyEvent> subs = new ArrayList<>();

	@Builder
	public MoneyEvent(User user, Room room, Long money) {
		this.token = TokenUtils.randomToken();
		this.user = user;
		this.room = room;
		this.money = money;
		this.recvExpDate = this.createdDate.plusMinutes(10);
		this.viewExpDate = this.createdDate.plusDays(7);
	}

	public List<SubMoneyEvent> distributeMoney(int headCount) {
		// 가능하면 분배 방식 전략 패턴 사용하도록 변경??

		Long money = this.money;
		long[] distributedMoneyArray = new long[headCount];
		int floor = (int) Math.pow(10, Math.log10(money));
		while (money != 0) {
			int subMoney = (int)(money / floor);
			distributedMoneyArray[(int)(Math.random() * headCount)] += subMoney;
			money -= subMoney;
			if (money < floor) {
				floor = (int) Math.pow(10, Math.log10(money));
			}
		}

		List<SubMoneyEvent> subEvents = new ArrayList<>();
		for (long distributedMoney : distributedMoneyArray) {
			subEvents.add(SubMoneyEvent.builder()
					.event(this)
					.money(distributedMoney)
					.user(user)
					.build());
		}
		return subEvents;
	}
}

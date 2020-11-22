package kakaopay.pretask.moneyapi.domain.event;

import kakaopay.pretask.moneyapi.domain.room.Room;
import kakaopay.pretask.moneyapi.domain.user.User;
import kakaopay.pretask.moneyapi.utils.token.TokenUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class SpreadMoney implements Serializable {

	private static final long serialVersionUID = -2782108821547888399L;

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
	private BigDecimal money;

	@Column(nullable = false)
	private Long headCount;

	//@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdDate;

	@Column(nullable = false)
	private LocalDateTime recvExpDate;

	@Column(nullable = false)
	private LocalDateTime viewExpDate;

	@OneToMany(mappedBy = "event")
	private List<ReceivedMoney> receivedMonies = new ArrayList<>();

	@Builder
	public SpreadMoney(User user, Room room, BigDecimal money, Long headCount) {
		this.token = TokenUtils.create();
		this.user = user;
		this.room = room;
		this.money = money;
		this.headCount = headCount;
		this.createdDate = LocalDateTime.now();
		this.recvExpDate = this.createdDate.plusMinutes(10);
		this.viewExpDate = this.createdDate.plusDays(7);
	}

	public boolean isSpreadUser(User user) {
		return this.getUser().equals(user);
	}

	public boolean isNotSpreadUser(User user) {
		return !isSpreadUser(user);
	}

	public boolean isReceivedUser(User user) {
		return this.receivedMonies.stream()
				.anyMatch(receivedMoney -> receivedMoney.isReceivedUser(user));
	}

	public boolean isAllReceived() {
		return this.headCount == this.receivedMonies.size();
	}

	public long getRemainedHeadCount() {
		return this.getHeadCount() - this.receivedMonies.size();
	}

	public BigDecimal getRemainedMoney() {
		return this.getMoney().subtract(receivedMoniesSum());
	}

	public BigDecimal receivedMoniesSum() {
		return this.receivedMonies.stream()
				.map(ReceivedMoney::getMoney)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

}

package kakaopay.pretask.moneyapi.domain.event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SubMoneyEventRepository extends CrudRepository<SubMoneyEvent, Long> {

	long countSubMoneyEventByUser_UserIdOrEvent_User_UserId(Long userId1, Long userId2);

	SubMoneyEvent findFirstByAssignedYnAndEvent_Token(char assignedYn, String token);
}

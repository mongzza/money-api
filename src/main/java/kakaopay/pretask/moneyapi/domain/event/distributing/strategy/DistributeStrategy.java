package kakaopay.pretask.moneyapi.domain.event.distributing.strategy;

public interface DistributeStrategy {
	long[] distribute(int headCount, long money);
}

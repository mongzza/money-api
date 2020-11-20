package kakaopay.pretask.moneyapi.domain.event.distributing.strategy;

public class RandomStrategy implements DistributeStrategy{

	@Override
	public long[] distribute(int headCount, long money) {
		long[] moneyArray = new long[headCount];
		int floor = (int) Math.pow(10, Math.log10(money));
		while (money != 0) {
			int subMoney = (int)(money / floor);
			moneyArray[(int)(Math.random() * headCount)] += subMoney;
			money -= subMoney;
			if (money < floor) {
				floor = (int) Math.pow(10, Math.log10(money));
			}
		}
		return moneyArray;
	}

}

package communications;

import java.io.Serializable;

/*/
 * @author Christoffer Palvin
 */
public class StartingInformation implements Serializable {

	private static final long serialVersionUID = 1863310895253088079L;
	private int numberOfMinutes;
	private int numberOfRounds;
	private int minimumBet;
	private int startingMoney;
	
	public StartingInformation(int minutes, int rounds, int miniBet, int startingMoney) {
		this.numberOfMinutes = minutes;
		this.numberOfRounds = rounds;
		this.minimumBet = miniBet;
		this.startingMoney = startingMoney;
	}
	
	public int getNumberOfMinutes() {
		return numberOfMinutes;
	}
	
	public int getNumberOfRounds() {
		return numberOfRounds;
	}
	
	public int getMinimumBet() {
		return minimumBet;
	}
	public int getStartingMoney() {
		return startingMoney;
	}

}

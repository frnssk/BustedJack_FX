package communications;

import java.io.Serializable;

public class StartingInformation implements Serializable {

	private static final long serialVersionUID = 1863310895253088079L;
	private int numberOfMinutes;
	private int numberOfRounds;
	private int minimumBet;
	
	public StartingInformation(int minutes, int rounds, int miniBet) {
		this.numberOfMinutes = minutes;
		this.numberOfRounds = rounds;
		this.minimumBet = miniBet;
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

}

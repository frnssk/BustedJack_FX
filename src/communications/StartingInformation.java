package communications;

public class StartingInformation {
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

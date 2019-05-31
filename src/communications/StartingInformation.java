package communications;

import java.io.Serializable;

/**
 * Sends required information for starting a game
 * @author Isak Eklund
 * @author Christoffer Palvin
 *
 */
public class StartingInformation implements Serializable {

	private static final long serialVersionUID = 1863310895253088079L;
	private int numberOfMinutes;
	private int numberOfRounds;
	private int minimumBet;
	private int startingMoney;
	
	/**
	 * Takes all parameters required for starting a game
	 * @param minutes - Number of minutes for the given game
	 * @param rounds - Number of rounds for the given game
	 * @param miniBet - Lowest bet allowed in the game
	 * @param startingMoney - The starting amount of money for each player 
	 */
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

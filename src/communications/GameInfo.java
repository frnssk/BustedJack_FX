package communications;

import java.io.Serializable;

/**
 * Object including all info required to start a new game
 * @author Isak Eklund
 *
 */
public class GameInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int time;
	private int rounds;
	private int balance;
	private int minBet;
	private boolean privateMatch;
	
	/**
	 * Constructor takes all the parameters needed for a new game
	 * @param time - number of minutes to play
	 * @param rounds - number of rounds to play
	 * @param balance - the balance each player get in the beginning of the game
	 * @param minBet - the lowest allowed bet during the game
	 * @param privateMatch - if the game is privat or not
	 */
	public GameInfo(int time, int rounds, int balance, int minBet, boolean privateMatch) {
		this.time = time;
		this.rounds = rounds;
		this.balance = balance;
		this.minBet = minBet;
		this.privateMatch = privateMatch;
	}

	public int getTime() {
		return time;
	}

	public int getRounds() {
		return rounds;
	}

	public int getBalance() {
		return balance;
	}

	public int getMinBet() {
		return minBet;
	}
	
	public boolean getPrivateMatchStatus() {
		return privateMatch;
	}
}

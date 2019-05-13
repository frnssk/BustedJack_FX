timepackage communications;

import java.io.Serializable;

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

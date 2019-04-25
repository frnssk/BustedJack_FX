package communications;

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
	
	public GameInfo(int time, int rounds, int balance, int minBet) {
		this.time = time;
		this.rounds = rounds;
		this.balance = balance;
		this.minBet = minBet;
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
}

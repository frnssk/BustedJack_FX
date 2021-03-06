package communications;

import java.io.Serializable;

/**
 * Create PlayerChoice objects to send from server to client when a player have made a play choice
 * @author Rasmus Öberg
 * @author Simon Lilja
 *
 */

public class PlayerChoice implements Serializable{

	private static final long serialVersionUID = 1L;
	private int choice;
	private int bet;
	private boolean cheatChoice = true; //remove =true
	private boolean splitChoice;
	
	public PlayerChoice(int choice) {
		this.choice = choice;
	}
	
	public void setCheatChoice(boolean choice) {
		this.cheatChoice = choice;
	}
	
	public boolean getCheatChoice() {
		return cheatChoice;
	}
	
	public int getChoice() {
		return choice;
	}
	
	public void setChoice(int choice) {
		this.choice = choice;
	}
	
	public void setBet(int bet) {
		this.bet = bet;
	}
	
	public int getBet() {
		return bet;
	}	
	
	public void setSplitChoice(boolean bool) {
		splitChoice = bool;
	}
	
	public boolean getSplitChoice() {
		return splitChoice;
	}
	
	public String toString() {
		return "int choice" + choice;
	}
	
	/**
	 * Resets all values to 0
	 */
	public void reset() {
		this.bet = 0;
		this.choice = 0;
		this.splitChoice = false;
	}
}

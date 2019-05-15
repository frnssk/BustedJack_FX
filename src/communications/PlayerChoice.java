package communications;

import java.io.Serializable;

public class PlayerChoice implements Serializable{

	private static final long serialVersionUID = 1L;
	private int choice;
	//1 = hit
	//2 = stay
	//3 = double
	//4 = bet, + setBet
	//5 = cheat
	private int bet;
	private boolean cheatChoice = true; //remove =true
	
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
	
	public String toString() {
		return "int choice" + choice;
	}
}

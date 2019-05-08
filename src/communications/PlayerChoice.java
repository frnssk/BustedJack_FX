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
	
	public PlayerChoice(int choice) {
		this.choice = choice;
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
}

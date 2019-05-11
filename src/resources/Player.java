package resources;

import java.io.Serializable;
import java.util.ArrayList;

import communications.PlayerChoice;

public class Player implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int startingBalance;
	private int betMade;
//	private Hand hand;
	private String username;
	private boolean hasMadeCheatChoice;
	private boolean cheatChoice;
	private boolean hasMadeBet;
	private ArrayList<Hand> hands = new ArrayList<>();
	private int cheatHeat;
	private int balance = 0;
	private Table table;
	private PlayerChoice playerChoice;
	private boolean buttonsAreGray;
	
	public Player(String username) {
		this.username = username;
		hands.add(new Hand());
	}
	
	public boolean areButtonsGray() {
		return buttonsAreGray;
	}

	public void setButtonsAreGray(boolean buttonsAreGray) {
		this.buttonsAreGray = buttonsAreGray;
	}
	
	public void setPlayerChoice(PlayerChoice playerChoice) {
		this.playerChoice = playerChoice;
		int choice = playerChoice.getChoice();
		if(choice == 1) {
			//hit
		}else if(choice == 2) {
			//stay
		}else if(choice == 3) {
			//double
		}else if(choice == 4) {
			//bet
			setHasMadeBet(true);
			setBet(playerChoice.getBet());
		}else if(choice == 5) {
			//cheat
			boolean cheatChoice = playerChoice.getCheatChoice();
			setCheatChoice(cheatChoice);
		}
		
	}
	
	public PlayerChoice getPlayerChoice() {
		return playerChoice;
	}
	
//	public void setTable(Table table) {
//		this.table = table;
//	}
	
//	public Table getTable() {
//		return table;
//	}
	
	//used to add a new hand when player is splitting
	public void addNewHand() {
		hands.add(new Hand());
	}
	//used to know how many hands a player is playing
	public int getNumberOfHands() {
		return hands.size();
	}
	//used to make plays on one specific hand
	public Hand getHand(int index) {
		return hands.get(index);
	}
	
	//used to set/get the players cheat-heat
	public void setCheatHeat(int cheatHeat) {
		this.cheatHeat = cheatHeat;
	}
	public int getCheatHeat() {
		return cheatHeat;
	}
	
	//Used by table when a player joins to get money to play with
	public void setStartingBalance(int startingBalance) {
		this.startingBalance = startingBalance;
	}
	
	//should be called by UI when a player presses "Cheat" or "No Cheat"
	//CORRECTION == should be determined by "setPlayerChoice"
	public void setCheatChoice(boolean choiceMade) {
		cheatChoice = choiceMade;
		hasMadeCheatChoice = true;
	}
	
	//used by table to check that the player has made a choice
	public boolean getHasMadeCheatChoice() {
		return hasMadeCheatChoice;
	}
	
	//used by table to decide if the player wants card from regular or cheat deck
	public boolean getCheatChoice() {
		return cheatChoice;
	}
	
	//used by server to set how much the player wants to bet
	public void setBet(int betMade) {
		this.betMade = betMade;
	}
	public int getBet() {
		return betMade;
	}
	
	//used by the table when the round starts to set a balance
	public void setBalance(int newBalance) {
		this.balance = newBalance;
	}
	public int getBalance() {
		return balance;
	}
	
	public void setHasMadeBet(boolean choice) {
		this.hasMadeBet = choice;
	}
	public boolean getHasMadeBet() {
		return hasMadeBet;
	}
	
	//checks if the player has made a bet
	public boolean getBetMade() {
		return (betMade != 0); //returns true if the betMade is not 0
	}
	
	public String getUsername() {
		return username;
	}

}

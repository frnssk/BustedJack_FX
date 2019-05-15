package resources;

import java.io.Serializable;
import java.util.ArrayList;

import communications.PlayerChoice;

public class Player implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int betMade;
	private String username;
	private boolean hasMadeCheatChoice;
	private boolean cheatChoice;
	private boolean hasMadeBet;
	private ArrayList<Hand> hands;
	private int cheatHeat;
	private int balance = 0;
	private PlayerChoice playerChoice;
	private boolean hasMadeChoice;
	
	public Player(String username) {
		hands = new ArrayList<>();
		this.username = username;
		hands.add(new Hand());
	}
	
	/*
	 * Used by server when a client makes a choice, connects the client to
	 * a player and sets the corresponding choice
	 */
	public void setPlayerChoice(PlayerChoice playerChoice) {
		this.playerChoice = playerChoice;
		System.out.println("PlayerChoice mottagit = " + this.playerChoice.getChoice());
//		hands.get(0).setPlayerChoice(this.playerChoice);
		for(int i = 0; i < hands.size(); i++) {
			while(!hands.get(i).getFinished()) {
				hands.get(i).setPlayerChoice(playerChoice);
			}
		}
		
		if(this.playerChoice.getChoice() == 4) {
			setHasMadeBet(true);
			setBet(this.playerChoice.getBet());
		}else if(this.playerChoice.getChoice() == 5) {
			setCheatChoice(this.playerChoice.getCheatChoice());
			setCheatChoice(cheatChoice);
		}
		System.out.println("playerChoice = " + playerChoice.toString());
	}
	
	public PlayerChoice getPlayerChoice() {
		return playerChoice;
	}
	
	public void setHasMadeChoice(boolean bool) {
		this.hasMadeChoice = bool;
	}
	
	public boolean getHasMadeChoice() {
		return hasMadeChoice;
	}
	
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
	
	//used by table to know when it can move on
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
	
	/*
	 * returns the players name
	 */
	public String getUsername() {
		return username;
	}

}

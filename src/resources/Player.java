package resources;

import java.io.Serializable;
import java.util.ArrayList;

import communications.PlayerChoice;

/**
 * Class that represents a player
 * @author rasmusoberg
 * @author Simon Lilja
 */
public class Player implements Serializable{
	
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
	private boolean playerIsOut;
	
	public Player(String username) {
		hands = new ArrayList<>();
		this.username = username;
		hands.add(new Hand());
	}
	
	/**
	 * Used when a user/client makes a choice, to forward the choice
	 * to the correct hand, if a player is playing multiple hands
	 * @param playerChoice
	 */
	public void setPlayerChoice(PlayerChoice playerChoice) {
		this.playerChoice = playerChoice;
		System.out.println("PlayerChoice mottagit = " + this.playerChoice.getChoice());
		for(int i = 0; i < hands.size(); i++) {
			boolean handIsReady = false;
			while(!handIsReady) {
				hands.get(i).setPlayerChoice(playerChoice);
				handIsReady = hands.get(i).getFinished();
			}
			
		}
		if(this.playerChoice.getChoice() == 4) {
			for(int i = 0; i < hands.size(); i++) {
				hands.get(i).setBet(this.playerChoice.getBet());
			
			setBet(this.playerChoice.getBet());
			setHasMadeBet(true);
			}
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
	
	/**
	 * When a player wants to split, he needs to get another hand
	 */
	public void addNewHand() {
		hands.add(new Hand());
	}

	public int getNumberOfHands() {
		return hands.size();
	}

	public Hand getHand(int index) {
		return hands.get(index);
	}
	
	public void setCheatHeat(int cheatHeat) {
		this.cheatHeat = cheatHeat;
	}
	
	public int getCheatHeat() {
		return cheatHeat;
	}
	
	public void setCheatChoice(boolean choiceMade) {
		cheatChoice = choiceMade;
		hasMadeCheatChoice = true;
	}
	
	public boolean getHasMadeCheatChoice() {
		return hasMadeCheatChoice;
	}
	
	public void setHasMadeCheatChoice(boolean bool) {
		this.hasMadeCheatChoice = bool;
	}
	
	public boolean getCheatChoice() {
		return cheatChoice;
	}
	
	public void setBet(int betMade) {
		this.betMade = betMade;
	}
	public int getBet() {
		return betMade;
	}
	
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
	
	public boolean getBetMade() {
		return (betMade != 0); 
	}
	
	public String getUsername() {
		return username;
	}
	public void removeHands() {
		hands.clear();
	}

	public boolean isPlayerIsOut() {
		return playerIsOut;
	}

	public void setPlayerIsOut(boolean playerIsOut) {
		this.playerIsOut = playerIsOut;
	}

}

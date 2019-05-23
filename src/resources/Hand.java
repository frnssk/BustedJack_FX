package resources;

import java.io.Serializable;
import java.util.ArrayList;

import communications.PlayerChoice;
import resources.Card.Rank;

public class Hand implements Serializable {

	private static final long serialVersionUID = 9072394852087177248L;
	private ArrayList<Card> hand; //Holds the card in a hand
	private boolean hasBlackjack;
	private boolean bustedHand;
//	private boolean wantToSplit = false;
//	private boolean hasMadeSplitChoice = false;
	private boolean hasMadeInsuranceChoice = false;
	private boolean insuranceChoice = false;
	private int handIsWin;
	private int payout;
	private int bet;
	private boolean hasMadeEndingChoice;
	
	private PlayerChoice playerChoice;
	private int playChoice;
	private int betMade;
	private boolean cheatChoice;
	private boolean hasMadeBet;
	private boolean hasMadePlayChoice;
	private boolean finished = true;
	private boolean displayValue;

	
	/*
	 * Constructor
	 */
	public Hand() {
		hand = new ArrayList<>();
	}
	
	/*
	 * Used by the server --> player to set a choice for the hand
	 */
	public void setPlayerChoice(PlayerChoice playerChoice) {
		this.playerChoice = playerChoice;
		int choice = this.playerChoice.getChoice();
		setPlayChoice(choice);
//		setHasMadePlayChoice(true);
	}

	public PlayerChoice getPlayerChoice() {
		return playerChoice;
	}
	
	/*
	 * Used to know that a player has made a choice, and is ready
	 */
	public void setHasMadePlayChoice(boolean bool) {
		this.hasMadePlayChoice = bool;
	}
	public boolean getHasMadePlayChoice() {
		return hasMadePlayChoice;
	}
	
	public void setPlayChoice(int choice) {
		setHasMadePlayChoice(true);
		this.playChoice = choice;
//		if(choice == 4) {
//			//bet
//			setHasMadeBet(true);
//			setBet(playerChoice.getBet());
//		}
	}
	
	public void setFinished(boolean bool) {
		finished = bool;
	}
	
	public boolean getFinished() {
		return finished;
	}
	
	public int getPlayChoice() {
		return playChoice;
	}
	
	public void setHasMadeBet(boolean bool) {
		this.hasMadeBet = bool;
	}

	public boolean getHasMadeEndingChoice() {
		return hasMadeEndingChoice;
	}
	public void setHasMadeEndingChoice(boolean hasMadeEndingChoice) {
		this.hasMadeEndingChoice = hasMadeEndingChoice;
	}

	/*
	 * Adds a new card to the hand
	 */
	public void addCard(Card card) {
		hand.add(card);
	}
	
	/*
	 * Needed to calculate the value of all cards, adjusted for ACE
	 */
	public int getCurrentScore() {
		int currentScore = 0;
		for(int i = 0; i < hand.size(); i++) {
			currentScore += hand.get(i).getValue();
		}
		if(this.containsAce() && (currentScore + 10) <= 21) {
			currentScore += 10;
		}
		return currentScore;
	}

	/*
	 * needed to adjust the value if a hand is soft/hard
	 */
	public boolean containsAce() {
		boolean contains = false;
		for(int i = 0; i < hand.size(); i++) {
			contains = (hand.get(i).getRank() == Rank.ACE);
			if(contains)
				break;
		}
		return contains;
	}
	
	public Card getCard() {
		return hand.remove(1);
	}
	
	public boolean ableToSplit() {
		return hand.get(0).equals(hand.get(1));
	}
	
//	public void setSplitChoice() {
//		this.hasMadeSplitChoice = true;
//	}
	
	public boolean getSplitChoice() {
		return playerChoice.getSplitChoice();
	}
//	
//	public boolean wantToSplit() {
//		return wantToSplit;
//	}
//	
//	//ui have to send a boolean depending on what button the user presser
//	public void setWantToSplit(boolean wantToSplit) {
//		this.wantToSplit = wantToSplit;
//		setSplitChoice();
//	}
	
	public void setHasMadeInsuranceChoice() {
		hasMadeInsuranceChoice = true;
	}
	
	public void setInsuranceChoice(boolean choice) {
		this.insuranceChoice = choice;
		setHasMadeInsuranceChoice();
	}
	
	public boolean getInsuranceChoice() {
		return insuranceChoice;
	}
	
	public boolean getHasMadeInsuranceChoice() {
		return hasMadeInsuranceChoice;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}
	
	public int getBet() {
		return bet;
	}
	
	public void setPayout(int payout) {
		this.payout = payout;
	}
	
	/*
	 * Needed to declare if a hand has beaten the dealer, saving for when the payout will happen
	 */
	public void setHandIsWin(int win) {
		handIsWin = win;
	}

	public int isHandWin() {
		return handIsWin;
		//1 = playerWin
		//-1 = dealerWin
		//0 - push
	}

	/*
	 * Needed to to specify a win of 150%
	 */
	public void setBlackjack(boolean hasBlackjack) {
		this.hasBlackjack = hasBlackjack;
	}

	public boolean hasBlackjack() {
		return hasBlackjack;
	}
	
	public void setBustedHand(boolean bustedHand) {
		this.bustedHand = bustedHand;
	}
	
	public boolean bustedHand() {
		return bustedHand;
	}

	/*
	 * Needed to count all the cards on the hand
	 */
	public int size() {
		return hand.size();
	}

	/*
	 * Clears the hand of all the cards
	 */
	public void clear() {
		hand.clear();
	}
	
	public String toString() {
		String string = "";
		for(int i = 0; i < size(); i++) {
			string += hand.get(i).toString() + ", ";
		}
		return string;
	}

	public boolean getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(boolean displayValue) {
		this.displayValue = displayValue;
	}

}

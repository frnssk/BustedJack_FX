package resources;

import java.io.Serializable;
import java.util.ArrayList;

import communications.PlayerChoice;
import resources.Card.Rank;

/**
 * Class that holds a number of cards, used by the player-class
 * @author rasmusoberg
 *
 */
public class Hand implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Card> hand; //Holds the card in a hand
	private boolean hasBlackjack;
	private boolean bustedHand;
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
	private String cardValue = "";
	private String Value = "";

	
	/**
	 * Constructor which creates a new ArrayList to hold the cards
	 */
	public Hand() {
		hand = new ArrayList<>();
	}
	
	/**
	 * Used when a user/client has made a choice on a hand, to determine what action should be done on it
	 * @param playerChoice - the choice made
	 */
	public void setPlayerChoice(PlayerChoice playerChoice) {
		this.playerChoice = playerChoice;
		int choice = this.playerChoice.getChoice();
		setPlayChoice(choice);
	}

	public PlayerChoice getPlayerChoice() {
		return playerChoice;
	}
	
	public void setHasMadePlayChoice(boolean bool) {
		this.hasMadePlayChoice = bool;
	}
	public boolean getHasMadePlayChoice() {
		return hasMadePlayChoice;
	}
	
	public void setPlayChoice(int choice) {
		setHasMadePlayChoice(true);
		this.playChoice = choice;
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
	
	/**
	 * Used to clarify that a choice has been made that renders this hand unable to continue
	 * @param hasMadeEndingChoice
	 */
	public void setHasMadeEndingChoice(boolean hasMadeEndingChoice) {
		this.hasMadeEndingChoice = hasMadeEndingChoice;
	}

	public void addCard(Card card) {
		hand.add(card);
	}
	
	public String getCardValue() {
		for(int i = 0; i < hand.size(); i++) {
			cardValue +=String.valueOf(hand.get(i).getValue()) + "/ ";
	}
		return cardValue + "\n";
	}
	
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

	/**
	 * Need to determine if a hand contains an Ace, and in that
	 * case adjust the value if the score reaches 21 or over
	 * @return
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
		return hand.get(0).getValue() == hand.get(1).getValue();
	}
	
	public boolean getSplitChoice() {
		return playerChoice.getSplitChoice();
	}
	
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
	
	/**
	 * Uses an int to determine if a hand has beat the dealer, and therefore how much 
	 * the hand has won (different value for win/blackjack/push/lose)
	 * @param win
	 */
	public void setHandIsWin(int win) {
		handIsWin = win;
	}

	public int isHandWin() {
		return handIsWin;
	}

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

	public int size() {
		return hand.size();
	}

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

	/**
	 * Display-value determines if a hand should be displayed in the UI
	 * or that that UI should display the next one
	 * @return
	 */
	public boolean getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(boolean displayValue) {
		this.displayValue = displayValue;
	}

}

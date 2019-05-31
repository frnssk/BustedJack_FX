package resources;

import java.io.Serializable;
import java.util.ArrayList;

import resources.Card.Rank;

/**
 * Class that represents a dealers stack of cards
 * @author Rasmus Öberg
 * @author Simon Lilja
 *
 */
public class DealerHand implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Card> dealerHand;
	
	/**
	 * Constructor
	 */
	public DealerHand() {
		this.dealerHand = new ArrayList<>();
	}
	
	public void addCard(Card card) {
		dealerHand.add(card);
	}
	
	public Card getCard(int index) {
		return dealerHand.get(index);
	}
	
	public int getValue() {
		int currentScore = 0;
		for(int i = 0; i < dealerHand.size(); i++) {
			currentScore += dealerHand.get(i).getValue();
		}
		if(this.containsAce() && (currentScore + 10) <= 21) {
			currentScore += 10;
		}
		return currentScore;
	}
	
	/**
	 * Used to modify the score if the dealer gets "fat" with an Ace
	 * on the hand
	 * @return
	 */
	public boolean containsAce() {
		boolean contains = false;
		for(int i = 0; i < dealerHand.size(); i++) {
			contains = (dealerHand.get(i).getRank() == Rank.ACE);
			if(contains)
				break;
		}
		return contains;
	}
	
	public String toString() {
		String string = "";
		for(int i = 0; i < dealerHand.size(); i++) {
			string += dealerHand.get(i).toString() + ", ";
		}
		return string;
	}
	public void clear() {
		dealerHand.clear();
	}
	

}

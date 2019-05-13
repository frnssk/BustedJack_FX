package resources;

import java.io.Serializable;
import java.util.ArrayList;

import resources.Card.Rank;

public class DealerHand implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Card> dealerHand;
	private int currentScore;
	
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
		for(int i = 0; i < dealerHand.size(); i++) {
			currentScore += dealerHand.get(i).getValue();
		}
		return currentScore;
	}
	
	public boolean containsAce() {
		boolean contains = false;
		for(int i = 0; i < dealerHand.size(); i++) {
			contains = (dealerHand.get(i).getRank() == Rank.ACE);
		}
		return contains;
	}
	

}

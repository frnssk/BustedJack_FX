package resources;

import java.util.ArrayList;

import resources.Card.Rank;

public class DealerHand {
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

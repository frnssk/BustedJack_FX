package resources;

import java.util.ArrayList;

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

}

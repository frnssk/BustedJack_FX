package resources;

import java.io.Serializable;
import java.util.*;

public class Hand implements Serializable {

	private static final long serialVersionUID = 9072394852087177248L;
	private ArrayList<Card> hand = new ArrayList<>(); //Holds the card in a hand
	
	/*
	 * Adds a new card to the hand
	 */
	public void addCard(Card card) {
		hand.add(card);
	}
	
	/*
	 * Returns the current value of a hand
	 */
	public int getCurrentScore() {
		int currentScore = 0;
		for(int i = 0; i < hand.size(); i++) {
			currentScore += hand.get(i).getValue();
		}
		return currentScore;
	}
	
	public int size() {
		return hand.size();
	}
	
	/*
	 * Clears the hand of all the cards
	 */
	public void clear() {
		hand.clear();
	}

}

package resources;

import java.io.Serializable;
import java.util.*;

import resources.Card.Rank;

public class Hand implements Serializable {

	private static final long serialVersionUID = 9072394852087177248L;
	private ArrayList<Card> hand = new ArrayList<>(); //Holds the card in a hand
	private boolean hasBlackjack;
	private boolean handIsWin;
	private boolean containsAce;

	/*
	 * Adds a new card to the hand
	 */
	public void addCard(Card card) {
		hand.add(card);
		//		currentScore += card.getValue();
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

	//checks if a hand contains an Ace, needed to adjust value
	public boolean containsAce() {
		boolean contains = false;
		for(int i = 0; i < hand.size(); i++) {
			contains = (hand.get(i).getRank() == Rank.ACE);
		}
		return contains;
	}

	public void setHandIsWin(boolean win) {
		handIsWin = win;
	}

	public boolean isHandWin() {
		return handIsWin;
	}

	public void setBlackjack(boolean hasBlackjack) {
		this.hasBlackjack = hasBlackjack;
	}

	public boolean hasBlackjack() {
		return hasBlackjack;
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

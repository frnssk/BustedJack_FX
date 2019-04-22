package resources;

import java.io.Serializable;
import java.util.*;

/**
 * Class to simulate a cheat-deck, which only contains high-value cards
 * @author rasmusoberg
 *
 */

public class CheatDeck implements Serializable {
	
	private static final long serialVersionUID = 5746633204212909460L;
	private ArrayList<Card> cheatDeck = new ArrayList<>();
	private int i = 0;
	/*
	 * Adds all the cards with values above 8, or aces
	 */
	public CheatDeck() {
		for(Card.Suit suit : Card.Suit.values()) {
			for(Card.Rank rank : Card.Rank.values()) {
				Card temp = new Card(rank, suit);
				if(temp.getValue() > 8 || temp.getValue() == 1) {
					cheatDeck.add(temp);
				}
			}
		}
	}
	
	/*
	 * Deals the card on the top of the deck
	 */
	public Card dealCard() {
		Card card = cheatDeck.get(cheatDeck.size() - 1);
		cheatDeck.remove(card);
		return card;
	}
	
	/*
	 * Returns the size of the deck
	 */
	public int size() {
		return cheatDeck.size();
	}

}

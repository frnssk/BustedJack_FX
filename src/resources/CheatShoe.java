package resources;

import java.io.Serializable;
import java.util.*;

/**
 * Class to hold a number of cheat-decks
 * @author rasmusoberg
 *
 */
public class CheatShoe implements Serializable {

	private static final long serialVersionUID = -1587383457835082451L;
	private Stack<Card> cheatShoe = new Stack<>();
	private Stack<Card> usedCards = new Stack<>();
	
	public CheatShoe(int numberOfDecks) {
		for(int i = 0; i < numberOfDecks; i++) {
			addCheatDeck(new CheatDeck());
		}
	}
	
	public Card dealCard() {
		return cheatShoe.lastElement();
	}
	
	/*
	 * Adds a cheat-deck to the shoe
	 */
	private void addCheatDeck(CheatDeck cheatDeck) {
		for(int i = 0; i < cheatDeck.size(); i++) {
			cheatShoe.add(cheatDeck.dealCard());
		}
	}
	
	public int getRemainingCards() {
		return cheatShoe.size();
	}
	
	/*
	 * Shuffles the deck of cheat-cards
	 */
	public void shuffle() {
		Collections.shuffle(cheatShoe);
	}
	
	public void clear() {
		cheatShoe.clear();
	}

}

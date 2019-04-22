package resources;

import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

/**
 * Class that simulates the Shoe of the deck, which holds a given number of decks
 * @author rasmusoberg
 *
 */

public class Shoe implements Serializable {

	private static final long serialVersionUID = 300924650586658215L;
	private Stack<Card> shoe = new Stack<>();
	private Stack<Card> usedCards = new Stack<>();
	
	public Shoe(int numberOfDecks) {
		for(int i = 0; i < numberOfDecks; i++) {
			addDeck(new Deck());
		}
	}
	
	/*
	 * Moves
	 */
	public Card dealCard() {
		Card card = shoe.get(shoe.size() - 1);
//		usedCards.add(card);
		shoe.remove(card);
		return card;
	}

	/*
	 * Adds a new deck to the shoe, card by card
	 */
	private void addDeck(Deck deck) {
		for(int i = 0; i < deck.getSize(); i++) {
			shoe.add(deck.dealCard());
		}
	}
	
	public int getRemainingCards() {
		return shoe.size();
	}
	
	/*
	 * Shuffles the entire shoe
	 */
	public void shuffle() {
		Collections.shuffle(shoe);
	}

}

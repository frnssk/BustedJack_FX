package resources;

import java.io.Serializable;
import java.util.*;

/**
 * A shoe is a collection of decks
 * @author Rasmus Öberg
 *
 */
public class CheatShoe implements Serializable {

	private static final long serialVersionUID = -1587383457835082451L;
	private Stack<Card> cheatShoe = new Stack<>();
	private Stack<Card> usedCards = new Stack<>();
	
	/**
	 * A shoe is a collection of decks
	 * @param numberOfDecks
	 */
	public CheatShoe(int numberOfDecks) {
		for(int i = 0; i < numberOfDecks; i++) {
			addCheatDeck(new CheatDeck());
		}
	}
	
	public Card dealCard() {
		Card card = cheatShoe.lastElement();
		cheatShoe.remove(cheatShoe.lastElement());
		return card;
	}
	
	private void addCheatDeck(CheatDeck cheatDeck) {
		for(int i = 0; i < cheatDeck.size(); i++) {
			cheatShoe.add(cheatDeck.dealCard());
		}
	}
	
	public int getRemainingCards() {
		return cheatShoe.size();
	}
	
	public void shuffle() {
		Collections.shuffle(cheatShoe);
	}
	
	public void clear() {
		cheatShoe.clear();
	}

}

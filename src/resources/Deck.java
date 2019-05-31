package resources;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Class to simulate a regular deck of cards, containing 52 different cards
 * @author Rasmus Öberg
 * @author Isak Eklund
 *
 */

public class Deck implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Card> deck = new ArrayList<>();

	public Deck() {
		for(Card.Suit suit : Card.Suit.values()) {
			int i = 1;
			for(Card.Rank rank : Card.Rank.values()) {
				Card card = new Card(rank, suit);
				if(suit.toString().equalsIgnoreCase("hearts")) {
					card.setFace("cards/hjärter" + i + ".png");
					card.setVisibility(true);
				}
				if(suit.toString().equalsIgnoreCase("spades")) {
					card.setFace("cards/spader" + i + ".png");
					card.setVisibility(true);
				}
				if(suit.toString().equalsIgnoreCase("diamonds")) {
					card.setFace("cards/ruter" + i + ".png");
					card.setVisibility(true);
				}
				if(suit.toString().equalsIgnoreCase("clubs")) {
					card.setFace("cards/klöver" + i + ".png");
					card.setVisibility(true);
				}
				i++;
				deck.add(card);
			}
		}
	}

	public Card dealCard() {
		Card card = deck.get(deck.size() - 1);
		deck.remove(card);
		return card;
	}

	public int getSize() {
		return deck.size();
	}

	public String toString() {
		String str = "";
		for(int i = 0; i < deck.size(); i++) {
			String card = deck.get(i).toString();
			str += card + ", \n";
		}
		return str;
	}

}

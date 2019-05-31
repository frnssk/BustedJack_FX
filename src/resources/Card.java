package resources;

import java.io.Serializable;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.*;

/**
 * Class that represents a playing card, containing a rank, a suit and a picture
 * @author Rasmus Öberg
 * @author Isak Eklund
 *
 */
public class Card implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Rank RANK;
	private final Suit SUIT;
	private ImageIcon face;
	private ImageIcon back;
	private ImageIcon front;

	/**
	 * Constructor
	 * @param rank - the rank of the card
	 * @param suit - the suit of the card
	 */
	public Card(Rank rank, Suit suit){
		RANK = rank;
		SUIT = suit;
		this.back = new ImageIcon("cards/baksida.png");
	}
	
	public ImageIcon getFront() {
		return front;
	}
	
	public void setFace(String image) {
		this.face = new ImageIcon(image);
	}
	
	/**
	 * Because the dealers cards are shown face-down initially, it's needed to be
	 * able to hide the front of the card
	 * @param sideToShow
	 */
	public void setVisibility(boolean sideToShow) {
		if(sideToShow) {
			this.front = face;
		}else {
			this.front = back;
		}
	}
	
	public int getValue() {
		return RANK.value;
	}
	
	public Rank getRank() {
		return RANK;
	}
	
	public String toString() {
		return RANK + " of " + SUIT;
	}

	public Suit getSuit() {
		return SUIT;
	}
	

	/**
	 * Represents the different value a card can have, Ace through King, with specialized
	 * Blackjack-values on the highest of cards
	 * @author rasmusoberg
	 *
	 */
	public enum Rank {
		ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);
		
		private int value;
		
		Rank(int value){
			this.value = value;
		}
	}
	
	/**
	 * Represents the different suits a card can belong to
	 * @author rasmusoberg
	 *
	 */
	public enum Suit {
		HEARTS, SPADES, DIAMONDS, CLUBS;
		
		public String toString() {
			return name().toLowerCase();
		}
	}
	
}

package resources;

import java.io.Serializable;

import javax.swing.*;

public class Card implements Serializable {

	private static final long serialVersionUID = 4817008418676203400L;
	private final Rank RANK;
	private final Suit SUIT;
	private ImageIcon face;
	private ImageIcon back;

	
	public Card(Rank rank, Suit suit){
		RANK = rank;
		SUIT = suit;
		this.face = null;
	}
	
	public void setFace(String image) {
		this.face = new ImageIcon(image);
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
	
	public void setVisibility(boolean visible) {
		if(visible)
			setFace(FRAMSIDA);
		else
			setFace(BAKSIDA);
	}
	
	/*
	 * Different ranks a card can have, and the values of each rank
	 */
	public enum Rank {
		ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);
		
		private int value;
		
		Rank(int value){
			this.value = value;
		}
	}
	
	/*
	 * Different suits a card can have
	 */
	public enum Suit {
		HEARTS, SPADES, DIAMONDS, CLUBS;
		
		public String toString() {
			return name().toLowerCase();
		}
	}
	
}

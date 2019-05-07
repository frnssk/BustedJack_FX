package resources;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Class to simulate a regular deck of cards, containing 52 different cards
 * @author rasmusoberg
 *
 */

public class Deck implements Serializable {

	private static final long serialVersionUID = 6801396353820734819L;
	private ArrayList<Card> deck = new ArrayList<>();

	public Deck() {
		for(Card.Suit suit : Card.Suit.values()) {
			int i = 1;
			for(Card.Rank rank : Card.Rank.values()) {
				Card card = new Card(rank, suit);
				if(suit.toString().equalsIgnoreCase("hearts")) {
					card.setFace("cards/hjärter" + i + ".png");
					card.setVisibility(true);
					card.setSize(50, 100);
//					System.out.println("cards/hjärter " + i);
				}
				if(suit.toString().equalsIgnoreCase("spades")) {
					card.setFace("cards/spader" + i + ".png");
					card.setVisibility(true);
//					System.out.println("cards/spader " + i);
				}
				if(suit.toString().equalsIgnoreCase("diamonds")) {
					card.setFace("cards/ruter" + i + ".png");
					card.setVisibility(true);
//					System.out.println("cards/ruter " + i);
				}
				if(suit.toString().equalsIgnoreCase("clubs")) {
					card.setFace("cards/klöver" + i + ".png");
					card.setVisibility(true);
//					System.out.println("cards/klöver " + i);
				}
				i++;
				deck.add(card);
			}
		}
	}

	/*
	 * Deals the top card of the deck
	 */
	public Card dealCard() {
		Card card = deck.get(deck.size() - 1);
		deck.remove(card);
		return card;
	}

	/*
	 * Returns the size of the deck
	 */
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
	
	public void showCards(){
		for(int i = 0; i < deck.size(); i++) {
			ImageIcon image = deck.get(i).getFront();
//			JOptionPane.showMessageDialog(null, new ImageIcon("cards/baksida.png"));
			JOptionPane.showMessageDialog(null, image);
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}

	public static void main(String[] args) {
		Deck deck = new Deck();
		System.out.println(deck.toString());
		deck.showCards();
	}

}

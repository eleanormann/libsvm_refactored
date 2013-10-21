package classes;

import interfaces.Card;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A Singleton deck that on first call creates an ArrayList of Cards, 
 */
public class Deck {
	private static Deck deck = null;
	ArrayList<Card> cards;
	
	/**
	 * creates a deck of Card objects, one for each suit-value combo. 
	 * then pseudo-randomises them using Collections shuffle
	 */
	private Deck(){
		cards = new ArrayList<Card>();
		for(int suit = 1; suit <= 4; suit++){
			for(int value = 1; value <= 13; value++){
				 Card card = new CardImpl(suit,value);
				 cards.add(card);
			}
		}
		Collections.shuffle(cards);
	}
	
	static{
		deck = new Deck();
	}
	
	public static Deck getDeck(){
		return deck;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}
	
	/**
	 * deals a cord from the top of the pack by assigning dealtCard the first Card in the list,
	 * then removing that card from the Arraylist
	 * @return Card 
	 */
	public Card dealCard(){
		Card dealtCard = cards.get(0);
		cards.remove(0);
		return dealtCard;
	}

	
}

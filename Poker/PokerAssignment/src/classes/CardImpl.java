/**
 * 
 */
package classes;

import interfaces.Card;

/**
 * @author emann06
 * This should be as simple as possible, so I have avoided creating Suit and Value objects. 
 * Makes them easier to use in calculations but harder to restrict to specific final values 
 * 
 *  In this game the only class to make Cards is Deck, but this class could be used
 *  in any card game potentially.
 *  
 */
public class CardImpl implements Card {
	private final int suit; //this needs to be immutable after created (a card cannot change its value/suit
	private final int value; //I also want to limit them to certain values - whats a good way of doing this?
	private static final String[] SUIT_NAMES = {"Joker", "Diamonds", "Spades", "Hearts", "Clubs"};
	//contains zero so that the toString method treats a 1 as an ace (otherwise 0 = ace). Seems a bit messy
	private static final String[] VALUE_NAMES = {"Zero", "Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
	
	/**
	 * @param suit
	 * @param value
	 * 
	 * I want the Cards to be standard, so @throws IllegalArgumentException when out
	 * of range values/suit are passed to the constructor
	 */
	public CardImpl(int suit, int value){
		if (suit <1 || suit >4){
			throw new IllegalArgumentException("No such suit exists");
		}
		else{
			this.suit = suit;
		}
		if (value<1 || value >13 ){
			throw new IllegalArgumentException("That is not a valid card value");
		}
		else{
			this.value = value;
		}		
	}
	
	
	@Override
	public int getSuit() {
		return suit;
	}

	
	@Override
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		return VALUE_NAMES[getValue()] + " of " + SUIT_NAMES[getSuit()];
	}
	
}

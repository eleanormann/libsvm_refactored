package potentialHands;



import interfaces.Card;

import java.util.List;

import pokerHands.AbstractPokerHand;
/*
 * three consecutive cards of any suit
 * consecutives==3
 */
public class ThreeStraight extends AbstractPokerHand {

	public ThreeStraight(int rank, List<Card> keep, List<Card> discard){
		super(rank, keep, discard);
		
	}
	

}

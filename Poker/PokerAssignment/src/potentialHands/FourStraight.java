package potentialHands;

import java.util.ArrayList;
import java.util.List;

import pokerHands.AbstractPokerHand;
import interfaces.Card;
import interfaces.PokerHand;

/*
 * hand containing four consecutive cards of any suit
 *  consecutives.size()==4
 */
public class FourStraight extends AbstractPokerHand {

	public FourStraight(int rank, List<Card> keep, List<Card> discard){
		super(rank, keep, discard);
	
	}
	
}

package potentialHands;

import interfaces.Card;

import java.util.List;

import pokerHands.AbstractPokerHand;

/* two cards of same suit and jack or better
 * sameSuits.size() == 2 && highCards.size() == 2
 */
public class TwoHighSameSuit extends AbstractPokerHand {

	public TwoHighSameSuit(int rank, List<Card> keep, List<Card> discard){
		super(rank, keep, discard);
		
	}
	
}

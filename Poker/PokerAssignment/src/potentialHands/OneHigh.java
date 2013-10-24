package potentialHands;

import interfaces.Card;

import java.util.List;

import pokerHands.AbstractPokerHand;

/*hand containing one jack or better and no other potential hand
 * highCards==1
 */
public class OneHigh extends AbstractPokerHand {

	public OneHigh(int rank, List<Card> keep, List<Card> discard){
		super(rank, keep, discard);
	}

}

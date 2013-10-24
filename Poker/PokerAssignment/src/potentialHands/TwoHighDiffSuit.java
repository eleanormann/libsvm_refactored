package potentialHands;

import java.util.ArrayList;
import java.util.List;

import pokerHands.AbstractPokerHand;
import interfaces.Card;
import interfaces.PokerHand;

/*two cards jack or better but no better potential hand
 * highCards==2
 * 
 */
public class TwoHighDiffSuit extends AbstractPokerHand {

	public TwoHighDiffSuit(int rank, List<Card> keep, List<Card> discard){
		super(rank, keep, discard);
	}
	

}

package potentialHands;

import java.util.ArrayList;
import java.util.List;

import pokerHands.AbstractPokerHand;
import interfaces.Card;
import interfaces.PokerHand;

/*hand containing four cards of the same suit
 * sameSuit.size() = 4
 */
public class FourFlush extends AbstractPokerHand{
	
	public FourFlush(int rank, List<Card> keep, List<Card> discard){
		super(rank, keep, discard);
		
	}

}

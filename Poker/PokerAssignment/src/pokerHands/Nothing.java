package pokerHands;

import interfaces.Card;
import interfaces.PokerHand;

import java.util.ArrayList;
import java.util.List;


public class Nothing extends AbstractPokerHand {

	public Nothing(int rank, List<Card> keep, List<Card> discard){
		super(rank, keep, discard);
	}


}

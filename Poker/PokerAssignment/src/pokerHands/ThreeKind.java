package pokerHands;

import interfaces.Card;
import interfaces.PokerHand;

import java.util.ArrayList;
import java.util.List;

import classes.Calculator;


public class ThreeKind extends AbstractPokerHand {
	public ThreeKind(int rank, List<Card> keep, List<Card> discard){
		super(rank, keep, discard);
	}
	

}

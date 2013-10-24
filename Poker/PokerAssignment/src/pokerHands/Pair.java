package pokerHands;

import interfaces.Card;
import interfaces.PokerHand;

import java.util.ArrayList;
import java.util.List;

import classes.Calculator;



public class Pair extends AbstractPokerHand { 
	
	
	public Pair(int rank, List<Card> keep, List<Card> discard){
		super(rank, keep, discard);
	}
	
	
}

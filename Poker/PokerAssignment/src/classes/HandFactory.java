package classes;

import interfaces.Card;
import interfaces.PokerHand;

import java.util.ArrayList;
import java.util.List;

import pokerHands.Flush;
import pokerHands.FourKind;
import pokerHands.Nothing;
import pokerHands.Pair;
import pokerHands.Straight;
import pokerHands.ThreeKind;
import pokerHands.TwoPairs;
import potentialHands.FourFlush;
import potentialHands.FourStraight;
import potentialHands.OneHigh;
import potentialHands.ThreeStraight;
import potentialHands.TwoHighDiffSuit;
import potentialHands.TwoHighSameSuit;

/**
 * 
 * @author eleanormann
 * is this a PokerHand factory?
 */
public class HandFactory{
	private List<Card> cards;
	
	public HandFactory(List<Card> cards) {
		this.cards = cards;
	}

	/**
	 * 
	 * @param calcSameValues
	 * @param calcConsecValues
	 * @param calcSameSuits
	 * @return cardsToKeep
	 */
	public PokerHand createPokerHand(List<Card> calcSameValues, List<Card> calcConsecValues, List<Card> calcSameSuits) {
		PokerHand hand = null;			
			
		if(calcSameSuits.size()==5){ 
			hand = new Flush(2, calcSameSuits, new ArrayList<Card>());
		}
		else if(calcConsecValues.size()==5){
			hand = new Straight(3, calcConsecValues, new ArrayList<Card>());
		}
		else{
			if(calcSameValues.isEmpty()){
				hand = new Nothing(7, new ArrayList<Card>(), cards);
				//what to do here?
			}
			else if(calcSameValues.size()==2){
				hand = new Pair(6, calcSameValues, discard());
			}
			else if(calcSameValues.size()==3) {
				hand= new ThreeKind(5, calcSameValues, discard());
	
			}
			else if(calcSameValues.size()==4){
				if(calcSameValues.get(1).compare(calcSameValues.get(1), calcSameValues.get(2))==0){
					hand = new FourKind(1, calcSameValues, discard());
					
				}
				else{
					hand= new TwoPairs(4, calcSameValues, discard());
				}
			}
				
		}
		return hand;
	}
	
	/*this method doesn't verify that no scoring hand exists
	 * could add PokerHand hand = createPokerHand(...
	 * but that doubles the work if there is no way to get to this method without going through create.. first
	 * 
	 */
	public PokerHand checkPotentialHand(List<Card> calcSameValues, List<Card> calcConsecValues, List<Card> calcSameSuits, List<Card> calcHighCards){
		PokerHand hand = null;
		if(calcSameSuits.size()==4){
			hand = new FourFlush(8, calcSameSuits, discard());
		}else if(calcConsecValues.size()==4){
			hand = new FourStraight(9, calcConsecValues, discard());
		}else if(calcConsecValues.size()==3){
			hand = new ThreeStraight(10, calcConsecValues, discard());
		}else if(calcHighCards.size()==2){
			if(calcSameSuits.size()==2){
				hand = new TwoHighSameSuit(11,calcHighCards, discard());
			}else{
				hand = new TwoHighDiffSuit(12, calcHighCards, discard());
			}
		}else if(calcHighCards.size()==1){
			hand = new OneHigh(13, calcHighCards, discard());
		}else{
			hand = new Nothing(14, new ArrayList<Card>(), cards);
		}
		return hand;
	}

	private List<Card> discard() {
		// TODO Auto-generated method stub
		return null;
	}
	
}


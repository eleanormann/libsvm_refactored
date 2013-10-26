package tests;

import static org.junit.Assert.*;
import interfaces.Card;
import interfaces.PokerHand;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import classes.CardImpl;
import classes.HandFactory;

public class TestHandFactory {
	public static HandFactory factory;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(1,10));
		cards.add(new CardImpl(3,1));
		cards.add(new CardImpl(4,7));
		cards.add(new CardImpl(3,6));
		factory = new HandFactory(cards);
	}

	@Test
	public void testGetCards(){
		assertEquals(5, factory.getCards().size());
	}
	
	@Test
	public void testGetDiscards(){
		assertEquals(5, factory.getDiscards().size());
	}
	
	@Test
	public void testDiscard(){
		List<Card> keeps = new ArrayList<Card>();
		keeps.add(new CardImpl(1,1));
		keeps.add(new CardImpl(1,10));
		factory.discard(keeps);
		assertEquals(3, factory.getDiscards().size());
	}
	
	@Test
	public void testCreatePokerHand4kind() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(2,1));
		cards.add(new CardImpl(3,1));
		cards.add(new CardImpl(4,1));
		cards.add(new CardImpl(3,6));
		factory = new HandFactory(cards);
		List<Card> sameVals = new ArrayList<Card>();
		sameVals.add(new CardImpl(1,1));
		sameVals.add(new CardImpl(2,1));
		sameVals.add(new CardImpl(3,1));
		sameVals.add(new CardImpl(4,1));
		List<Card> consecValues = new ArrayList<Card>();
		List<Card> sameSuits = new ArrayList<Card> ();
		sameSuits.add(new CardImpl(3,1));
		sameSuits.add(new CardImpl(3,6));
		PokerHand hand = factory.createPokerHand(sameVals, consecValues, sameSuits);
		assertEquals(1, factory.getDiscards().size());
		assertEquals(1, hand.getRank());
	}
	
	@Test
	public void testCreatePokerHandFlush() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(1,10));
		cards.add(new CardImpl(1,3));
		cards.add(new CardImpl(1,7));
		cards.add(new CardImpl(1,6));
		factory = new HandFactory(cards);
		List<Card> calcSameValues = new ArrayList<Card>();
		List<Card> calcConsecValues = new ArrayList<Card>();
		PokerHand hand = factory.createPokerHand(calcSameValues, calcConsecValues, cards);
		assertEquals(0, factory.getDiscards().size());
		assertEquals(2, hand.getRank());
	}
	
	@Test
	public void testCreatePokerHandNothing() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(1,8));
		cards.add(new CardImpl(3,3));
		cards.add(new CardImpl(2,11));
		cards.add(new CardImpl(4,6));
		factory = new HandFactory(cards);
		List<Card> calcSameValues = new ArrayList<Card>();
		List<Card> calcConsecValues = new ArrayList<Card>();
		List<Card> calcSameSuits = new ArrayList<Card>();
		calcSameSuits.add(new CardImpl(1,1));
		calcSameSuits.add(new CardImpl(1,8));
		PokerHand hand = factory.createPokerHand(calcSameValues, calcConsecValues, calcSameSuits);
		assertEquals(0, factory.getDiscards().size());
		assertEquals(7, hand.getRank());
	}
	
	@Test
	public void testCreatePokerHandPair() {
		List<Card> calcSameValues = new ArrayList<Card>();
		List<Card> calcConsecValues = new ArrayList<Card>();
		List<Card> calcSameSuits = new ArrayList<Card>();
		calcSameSuits.add(new CardImpl(1,1));
		calcSameSuits.add(new CardImpl(1,8));
		PokerHand hand = factory.createPokerHand(calcSameValues, calcConsecValues, calcSameSuits);
		assertEquals(0, factory.getDiscards().size());
		assertEquals(1, hand.getRank());
	}
	
	@Test
	public void testCreatePokerHandStraight() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(1,2));
		cards.add(new CardImpl(3,3));
		cards.add(new CardImpl(2,4));
		cards.add(new CardImpl(4,5));
		factory = new HandFactory(cards);
		List<Card> calcSameValues = new ArrayList<Card>();
		List<Card> calcConsecValues = new ArrayList<Card>();
		calcConsecValues.addAll(cards);
		List<Card> calcSameSuits = new ArrayList<Card>();
		PokerHand hand = factory.createPokerHand(calcSameValues, calcConsecValues, calcSameSuits);
		assertEquals(0, factory.getDiscards().size());
		assertEquals(3, hand.getRank());
	}
	
	@Test
	public void testCreatePokerHand3Kind() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(2,1));
		cards.add(new CardImpl(3,1));
		cards.add(new CardImpl(4,4));
		cards.add(new CardImpl(3,6));
		factory = new HandFactory(cards);
		List<Card> sameVals = new ArrayList<Card>();
		sameVals.add(new CardImpl(1,1));
		sameVals.add(new CardImpl(2,1));
		sameVals.add(new CardImpl(3,1));
		List<Card> consecValues = new ArrayList<Card>();
		List<Card> sameSuits = new ArrayList<Card> ();
		sameSuits.add(new CardImpl(3,1));
		sameSuits.add(new CardImpl(3,6));
		PokerHand hand = factory.createPokerHand(sameVals, consecValues, sameSuits);
		assertEquals(2, factory.getDiscards().size());
		assertEquals(5, hand.getRank());
	}
	
	@Test
	public void testCreatePokerHand2pair() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(2,1));
		cards.add(new CardImpl(3,9));
		cards.add(new CardImpl(4,9));
		cards.add(new CardImpl(3,6));
		factory = new HandFactory(cards);
		List<Card> sameVals = new ArrayList<Card>();
		sameVals.add(new CardImpl(1,1));
		sameVals.add(new CardImpl(2,1));
		sameVals.add(new CardImpl(3,9));
		sameVals.add(new CardImpl(4,9));
		List<Card> consecValues = new ArrayList<Card>();
		List<Card> sameSuits = new ArrayList<Card> ();
		sameSuits.add(new CardImpl(3,9));
		sameSuits.add(new CardImpl(3,6));
		PokerHand hand = factory.createPokerHand(sameVals, consecValues, sameSuits);
		assertEquals(1, factory.getDiscards().size());
		assertEquals(4, hand.getRank());
	}
	
	@Test
	public void testCreatePotentialHand4Flush() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(1,10));
		cards.add(new CardImpl(1,3));
		cards.add(new CardImpl(1,7));
		cards.add(new CardImpl(4,6));
		factory = new HandFactory(cards);
		List<Card> calcSameValues = new ArrayList<Card>();
		List<Card> calcHighCards = new ArrayList<Card>();
		calcHighCards.add(new CardImpl(1,1));
		List<Card> calcConsecValues = new ArrayList<Card>();
		calcConsecValues.add(new CardImpl(1,7));
		calcConsecValues.add(new CardImpl(4,6));
		List<Card> calcSameSuits = new ArrayList<Card>();
		calcSameSuits.add(new CardImpl(1,1));
		calcSameSuits.add(new CardImpl(1,10));
		calcSameSuits.add(new CardImpl(1,3));
		calcSameSuits.add(new CardImpl(1,7));
		PokerHand hand = factory.checkPotentialHand(calcSameValues, calcConsecValues, calcSameSuits, calcHighCards);
		assertEquals(1, factory.getDiscards().size());
		assertEquals(8, hand.getRank());
	}

	@Test
	public void testCreatePotentialHand4Straight() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(1,2));
		cards.add(new CardImpl(3,3));
		cards.add(new CardImpl(2,4));
		cards.add(new CardImpl(4,9));
		factory = new HandFactory(cards);
		List<Card> calcSameValues = new ArrayList<Card>();
		List<Card> calcConsecValues = new ArrayList<Card>();
		calcConsecValues.addAll(cards);
		calcConsecValues.remove(4);
		List<Card> calcSameSuits = new ArrayList<Card>();
		calcSameSuits.add(new CardImpl(1,1));
		calcSameSuits.add(new CardImpl(1,2));
		List<Card> calcHighCards = new ArrayList<Card>();
		calcHighCards.add(new CardImpl(1,1));
		PokerHand hand = factory.checkPotentialHand(calcSameValues, calcConsecValues, calcSameSuits, calcHighCards);
		assertEquals(1, factory.getDiscards().size());
		assertEquals(9, hand.getRank());
	}

	@Test
	public void testCreatePotentialHand3Straight() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(1,2));
		cards.add(new CardImpl(3,3));
		cards.add(new CardImpl(2,6));
		cards.add(new CardImpl(4,9));
		factory = new HandFactory(cards);
		List<Card> calcSameValues = new ArrayList<Card>();
		List<Card> calcConsecValues = new ArrayList<Card>();
		calcConsecValues.add(cards.get(0));
		calcConsecValues.add(cards.get(1));
		calcConsecValues.add(cards.get(2));
		List<Card> calcSameSuits = new ArrayList<Card>();
		calcSameSuits.add(new CardImpl(1,1));
		calcSameSuits.add(new CardImpl(1,2));
		List<Card> calcHighCards = new ArrayList<Card>();
		calcHighCards.add(new CardImpl(1,1));
		PokerHand hand = factory.checkPotentialHand(calcSameValues, calcConsecValues, calcSameSuits, calcHighCards);
		assertEquals(2, factory.getDiscards().size());
		assertEquals(10, hand.getRank());
	}

	@Test
	public void testCreatePotentialHand1High() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(1,8));
		cards.add(new CardImpl(3,3));
		cards.add(new CardImpl(2,10));
		cards.add(new CardImpl(4,6));
		factory = new HandFactory(cards);
		List<Card> calcSameValues = new ArrayList<Card>();
		List<Card> calcConsecValues = new ArrayList<Card>();
		List<Card> calcSameSuits = new ArrayList<Card>();
		calcSameSuits.add(new CardImpl(1,1));
		calcSameSuits.add(new CardImpl(1,8));
		List<Card> calcHighCards = new ArrayList<Card>();
		calcHighCards.add(new CardImpl(1,1));
		PokerHand hand = factory.checkPotentialHand(calcSameValues, calcConsecValues, calcSameSuits, calcHighCards);
		assertEquals(4, factory.getDiscards().size());
		assertEquals(13, hand.getRank());
	}

	@Test
	public void testCreatePotentialHand2HighSame() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(1,11));
		cards.add(new CardImpl(3,3));
		cards.add(new CardImpl(2,10));
		cards.add(new CardImpl(4,6));
		factory = new HandFactory(cards);
		List<Card> calcSameValues = new ArrayList<Card>();
		List<Card> calcConsecValues = new ArrayList<Card>();
		List<Card> calcSameSuits = new ArrayList<Card>();
		calcSameSuits.add(new CardImpl(1,1));
		calcSameSuits.add(new CardImpl(1,11));
		List<Card> calcHighCards = new ArrayList<Card>();
		calcHighCards.add(new CardImpl(1,1));
		calcHighCards.add(new CardImpl(1,11));
		PokerHand hand = factory.checkPotentialHand(calcSameValues, calcConsecValues, calcSameSuits, calcHighCards);
		assertEquals(3, factory.getDiscards().size());
		assertEquals(11, hand.getRank());
	}
	@Test
	public void testCreatePotentialHand2HighDiff() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(4,11));
		cards.add(new CardImpl(3,3));
		cards.add(new CardImpl(2,10));
		cards.add(new CardImpl(4,6));
		factory = new HandFactory(cards);
		List<Card> calcSameValues = new ArrayList<Card>();
		List<Card> calcConsecValues = new ArrayList<Card>();
		List<Card> calcSameSuits = new ArrayList<Card>();
		calcSameSuits.add(new CardImpl(4,6));
		calcSameSuits.add(new CardImpl(4,11));
		List<Card> calcHighCards = new ArrayList<Card>();
		calcHighCards.add(new CardImpl(1,1));
		calcHighCards.add(new CardImpl(4,11));
		PokerHand hand = factory.checkPotentialHand(calcSameValues, calcConsecValues, calcSameSuits, calcHighCards);
		assertEquals(3, factory.getDiscards().size());
		assertEquals(11, hand.getRank());
	}



}

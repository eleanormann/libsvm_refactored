package tests;

import static org.junit.Assert.*;
import interfaces.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import classes.CardCalculator;
import classes.CardImpl;

public class TestCardCalculator {
	public static CardCalculator calc;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new CardImpl(1,1));
		cards.add(new CardImpl(1,10));
		cards.add(new CardImpl(3,1));
		cards.add(new CardImpl(4,7));
		cards.add(new CardImpl(3,6));
		calc = new CardCalculator(cards);
	}

	/* A calculator that does not know about Cards ran into difficulties, so this class is a Card calculator
	 * Calculator needs to take 5 Card objects and identify which cards represent the best existing cardsToKeep, and
	 * which cards can be discarded or swapped
	 *
	 * All decisions are based on: 
	 * i) number of same values;
	 * ii) number of same suits
	 * iii) number of consecutive values
	 * 
	 * but unlike the other calculator this one needs to do the type transformations and 
	 * set some PokerHand attributes
	 * 
	 * 
	 */
	@Test
	public void testCalculatorHasCards() {
		assertNotNull(calc.getCards());
		
	}
	
	
	@Test
	public void testCollectionsSort() {
		Collections.sort(calc.getCards(), new CardImpl(1,1));
		boolean diff = true;
		int i = 1;
		while(i<5){
			if(calc.getCards().get(i).getValue()-calc.getCards().get(i-1).getValue()<0){
				diff=false;
			}
			i++;
		}
		assertTrue(diff);
	}
	
	/*
	 * test using true consecutives and one sequence
	 */
	@Test
	public void testConsecCardCalcDiff1Seq1(){
		List<Card> consecs = calc.consecCardComparer(1);
		assertEquals(2, consecs.size());
	}

	/*
	 * test using true consecutives and two sequences; first largest
	 */
	@Test
	public void testConsecCardCalcDiff1Seq2(){
		List<Card> newcards = new ArrayList<Card>();
		newcards.add(new CardImpl(1,1));
		newcards.add(new CardImpl(1,2));
		newcards.add(new CardImpl(3,3));
		newcards.add(new CardImpl(4,7));
		newcards.add(new CardImpl(3,6));
		calc = new CardCalculator(newcards);
		List<Card> consecs = calc.consecCardComparer(1);
		int i = 0;
		
		assertEquals(3, consecs.size());
	}
	/*
	 * test using true consecutives and two sequences; same size, with matching suit for the second
	 * note that the 7 chosen depends on the order drawn - need to add something to ensure same suit consecs are chosen
	 */
	@Test
	public void testConsecCardCalcDiff1Seq2a(){
		List<Card> newcards = new ArrayList<Card>();
		newcards.add(new CardImpl(1,1));
		newcards.add(new CardImpl(1,2));
		newcards.add(new CardImpl(4,7));
		newcards.add(new CardImpl(3,7));
		newcards.add(new CardImpl(3,6));
		calc = new CardCalculator(newcards);
		List<Card> consecs = calc.consecCardComparer(1);
		int i = 0;
		while(i<consecs.size()){
			System.out.println(consecs.get(i).toString());
			i++;
		}
		assertEquals(2, consecs.size());
	}
}

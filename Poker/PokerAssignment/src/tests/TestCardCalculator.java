package tests;

import static org.junit.Assert.*;
import interfaces.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;


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
	public void testGetValuesAndSuits() {
		fail("Not yet implemented");
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
	

}

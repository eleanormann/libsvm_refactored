package tests;

import static org.junit.Assert.*;
import interfaces.Card;

import org.junit.Test;

import classes.CardImpl;

public class TestCard {

	@Test
	public void testGetSuit() {
		Card testCard = new CardImpl(1,1);
		int expected = 1;
		int output = testCard.getSuit();
		assertEquals(expected,output);
	}
	
	@Test
	public void testGetValue() {
		Card testCard = new CardImpl(1,1);
		int expected = 1;
		int output = testCard.getValue();
		assertEquals(expected,output);
	}
	
	/**
	 * tests for invalid card fields
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetSuitException() {
		Card testCard = new CardImpl(5,6);
		int expected = 5;
		int output = testCard.getSuit();
		assertEquals(expected,output);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetValueException() {
		Card testCard = new CardImpl(1,14);
		int expected = 14;
		int output = testCard.getValue();
		assertEquals(expected,output);
	}
	
	/*
	 * OK, I get the point of having a dummy card at position 0 now...
	 */
	@Test
	public void testToString() {
		Card testCard = new CardImpl(2,1);
		String expected = "Ace of Spades";
		String output = testCard.toString();
		assertEquals(expected,output);
	}
	
	/*
	 * test for same values
	 */
	@Test
	public void testComparator(){
		Card card1 = new CardImpl(1,1);
		Card card2 = new CardImpl(2,1);
		assertEquals(0, card1.compare(card1, card2));
	}

	/*
	 * test for card1 being lower than card 2
	 */
	@Test
	public void testComparatorCard1Lower(){
		Card card1 = new CardImpl(1,1);
		Card card2 = new CardImpl(2,2);
		assertEquals(-1, card1.compare(card1, card2));
	}
	/*
	 * test for card2 being lower than card1
	 */
	@Test
	public void testComparatorCard2Lower(){
		Card card1 = new CardImpl(1,2);
		Card card2 = new CardImpl(2,1);
		assertEquals(1, card1.compare(card1, card2));
	}
}

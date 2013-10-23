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
	
	/**
	 * OK, I get the point of having a dummy card at position 0 now...
	 */
	@Test
	public void testToString() {
		Card testCard = new CardImpl(1,1);
		String expected = "Ace of Spades";
		String output = testCard.toString();
		assertEquals(expected,output);
	}
	
	

}

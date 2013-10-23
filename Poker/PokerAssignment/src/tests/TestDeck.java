package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import interfaces.Card;


import org.junit.Test;

import classes.Deck;


public class TestDeck {
	
	/*test whether static class and private constructor work
	 */
	@Test
	public void testDeckImpl() {
		assertNotNull(Deck.getDeck());
	}
	
	/*
	 * test that full list of cards created
	 */
	@Test
	public void testGetCards(){
		assertEquals(52, Deck.getDeck().getCards().size());
	}
	
	/*
	 * test that cannot create new deck
	 */
	@Test (expected= java.lang.Error.class)
	public void testCannotCreateAnotherDeck(){
		Deck deck = new Deck();
		assertNull(deck);
	}
	
	/*
	 * test that a Card object is returned
	 */
	@Test
	public void testDealCard(){
		assertEquals("CardImpl", Deck.getDeck().dealCard().getClass().getSimpleName());
	}
		
		
	/*
	 * tests that the first card added to the list is not still Card 1:1
	 * not a definitive test as it will be by chance. also does not test the quality
	 * of the shuffle, but I'll trust it because its a Collections algorithm, so it'll be adequate	
	 */
	@Test
	public void testDeckShuffled() {
		assertFalse(Deck.getDeck().dealCard().toString().equals("Ace of Diamonds"));
	}

	

}

package tests;

import static org.junit.Assert.*;

import interfaces.Dealer;
import interfaces.PokerHand;

import org.junit.BeforeClass;
import org.junit.Test;

import classes.DealerImpl;

;

public class DealerTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}


	@Test
	public void testDealCards() {
		Dealer testDealer = new DealerImpl();
		PokerHand testHand = testDealer.dealCards();
		assertEquals(5, testHand.getTheActualCards().size());
	}

	@Test
	public void testFindBestHand() {
		fail("Not yet implemented");
	}

	@Test
	public void testDetermineWinner() {
	
		fail("Not yet implemented");
	}

	@Test
	public void testDetermineDiscard() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testPotentialHand(){
		
	}
	

}

package interfaces;

import java.util.ArrayList;
/**
 * 
 * @author eleanormann
 * 
 * DealerImpl coordinates the game. 
 * 
 * 1. deal two five-card poker hands, dealer's face down
 * 
 * 2. Evaluate either player's hand for best hand and reevaluate as needed
 *
 * 3. evaluate dealer's hand for best potential hand, and choose up to 3 cards to discard and replace.
 *
 * 4. allow the player to decide which of the cards in their hand should be replaced 
 *
 * 5. Draw one, two, or three more cards to replace the corresponding number of
 *unneeded cards in the player's original hand. 
 *
 * 6. compare both hands (the dealer and the player) and determine who wins 
 * 
 *
 */
public interface Dealer {
	
	
	/**
	 * 'start' method to allow game parameters to be set as needed
	 * @param otherPlayer
	 */
	public void play(String otherPlayer);
	
	/**
	 * 
	 * @return PokerHand
	 */
	public PokerHand dealCards();
	
	/**
	 * 
	 * @param card
	 * @return true if successfully removed from hand
	 */
	public PokerHand replaceCards(ArrayList<Card> cards);
	
	
	public String determineWinner();
	
	/**This looks like an afterthought
	 * Its a method for automating the choice of best hand. Used by computer dealer, but also can be used 
	 * in a feature for user to ask computer to find best hand
	 * @return
	 */
	public PokerHand chooseCardsToReplace();

	

	
	

	
}

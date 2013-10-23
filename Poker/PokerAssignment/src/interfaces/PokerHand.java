package interfaces;

import java.util.ArrayList;


/**
 * @author eleanormann
 *PokerHands ranked according to the table shown in http://poker.about.com/od/cardroomscasinos/a/videopokertips.htm
 *(adapted for this version)
 */
public interface PokerHand {
	
	/**Each implementation of PokerHand has a rank order (see above)
	 * @return rank (1 = best)
	 */
	int getRank(); 
	
	/**Each implementation has an ArrayList of Cards making up the hand
	 * @param hand
	 */
	void setHand(ArrayList<Card> hand);
	
	/**
	 * this should be getHand() I dont know why I called it this. probably some clarity issue elsewhere
	 * @return
	 */
	ArrayList<Card> getTheActualCards();
	
	/**This method removes the cards that do not make up the hand, e.g. if it were a pair the 
	 * three non-pair cards would be added to a new list, removed from PokerHand.hand and the new list returned
	 * It is not crucial to the game to return the list but may be useful for GUI
	 * @return
	 */
	ArrayList<Card> discard();
}

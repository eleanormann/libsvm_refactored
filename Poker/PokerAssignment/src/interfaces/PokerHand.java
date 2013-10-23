package interfaces;

import java.util.List;


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
	
	/**Each implementation has an ArrayList of Cards making up the cardsToKeep
	 * @param cardsToKeep
	 */
	void setCardsToKeep(List<Card> hand);
	
	/**
	 *
	 * @return scoring cards
	 */
	List<Card> getCardsToKeep();
	
	/*
	 * this method could be deprecated now because I changed the design a bit
	 */
	/**This method removes the cards that do not make up the cardsToKeep, e.g. if it were a pair the 
	 * three non-pair cards would be added to a new list, removed from PokerHand.cardsToKeep and the new list returned
	 * It is not crucial to the game to return the list but may be useful for GUI
	 * @return
	 */
	List<Card> discard();

	void setCardsToDiscard(List<Card> arrayList);
}

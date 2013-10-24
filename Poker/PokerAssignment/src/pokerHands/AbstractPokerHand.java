package pokerHands;

import java.util.List;

import interfaces.Card;
import interfaces.PokerHand;

/*still needs a controlled way of setting rank, but flexible so royal flush can be added later
 * 
 */
public abstract class AbstractPokerHand implements PokerHand {
	protected List<Card> cardsToKeep;
	protected List<Card> cardsToDiscard;
	protected int rank; 
	
	
	public AbstractPokerHand(int rank){
		this.rank = rank;
	}

	public AbstractPokerHand(int rank, List<Card> cardsToKeep, List<Card> cardsToDiscard){
		this.rank = rank;
		this.cardsToKeep = cardsToKeep;
		this.cardsToDiscard = cardsToDiscard;
	}
	
	public int getRank() {
		return rank;
	}
	
	public List<Card> getCardsToKeep() {
		return cardsToKeep;
	}

	public void setCardsToKeep(List<Card> cardsToKeep) {
		this.cardsToKeep = cardsToKeep;
	}

	public List<Card> getCardsToDiscard() {
		return cardsToDiscard;
	}

	public void setCardsToDiscard(List<Card> cardsToDiscard) {
		this.cardsToDiscard = cardsToDiscard;
	}

	@Override
	public List<Card> discard() {
		return cardsToDiscard;
	}
	
	@Override
	public String toString(){
		int i = 0;
		String cards = "";
		while(i<cardsToKeep.size()){
			cards = cards + "\r" + (i+1) + ". " + cardsToKeep.get(i).toString(); 
			i++;
		}
		return cards;
		
	}

}

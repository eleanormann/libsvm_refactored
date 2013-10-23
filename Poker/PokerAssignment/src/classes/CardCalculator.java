package classes;

import interfaces.Card;
import interfaces.PokerHand;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collections;


public class CardCalculator {
	private List<Card> cards;

	public CardCalculator(List<Card> cards) {
		this.cards = cards;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
	
	public PokerHand createHand(){
		List<Card> sameSuits = new ArrayList<Card>();
		List<Card> sameValues = new ArrayList<Card>();
		List<Card> consecutiveValues = new ArrayList<Card>();
		
		Iterator<Card>it = cards.iterator();
		
		List<Card> temp = new ArrayList<Card>();
		while(it.hasNext()){
			temp =cardComparer(it.next().getSuit(), 1);
			if(temp.size()>=sameSuits.size()){
				sameSuits=temp;
			}
		}
		while(it.hasNext()){
			temp = cardComparer(it.next().getValue(), 2);
			if(temp.size()>=sameValues.size()){
				sameValues=temp;
			}
		}
		
		consecutiveValues = consecCardComparer(1);
		
		HandFactory hf = new HandFactory(cards);
		return hf.createPokerHand(sameValues, consecutiveValues, sameSuits);
	}
	
	
	/**Calculator needs to take 5 integer pairs and identify best existing cardsToKeep
	 * 
	 * i) number of same values;
	 * ii) number of same suits
	 * iii) number of consecutive values
	 * 
	 * 
	 */	
	public List<Card> cardComparer(int value, int suitOrValue){
		List<Card> returns = new ArrayList<Card>();
		Iterator<Card> it = cards.iterator();
		while (it.hasNext()){
			Card thisCard = it.next();
			if(suitOrValue==1){
				if(thisCard.getSuit()==value){
					returns.add(thisCard);
				}
			}else if(suitOrValue==2){
				if(thisCard.getValue()==value){
					returns.add(thisCard);
				}
			}else{
				throw new IllegalArgumentException("that is not a suit or value type");
			}
		}
		return returns;
		
	}
	
	
	
	/**This method is used to check for straights and flushes, and to check for potential runs
	 * It checks whether there is a difference of 1 or 2 between consecutive values in a sorted array (depending on type)
	 * and keeps a running count of the size of the run. the largest run is indexed by max, and the values are added to a list 
	 * that is returned at the end. It favours higher value runs of the same length by replacing same length runs by \
	 * those later in the sorted list. 
	 * 
	 * @param values is an array of values (Card values)
	 * @param type specifies whether checking for a consecutive run (i.e difference of 1) or whether checking 
	 * for potential runs (return values with difference of 1 or 2)
	 * @return ArrayList<Integer>
	 */

	public List<Card> consecCardComparer(int type){
		List<Card> returns = new ArrayList<Card>();
		Collections.sort(cards, new CardImpl(1,1));
		/*was going to do it the loop with the iterator but I need the index
		 */
		int i = 1;
		int max = 0;
		int count = 0;
		
		Card prevCard = cards.get(0);
		while ( i< cards.size()){
			//get the card at i and set to current card
			Card thisCard = cards.get(i);
			
			//first identify the range of consecutive values
			if(matchCriteria(prevCard.getValue(), thisCard.getValue(), type)==true){
				//if its the first in the range add it
				if(count==0){
					count++;
				}
				//then add subsequent counts
				count++;
			//when there is a break in the sequence or no sequence	
			}else{
				
				//check that not no sequence, and that sequence is longer than prev sequence
				if(count!=0 && count>=max){
					
					//remove prev sequence
					returns.removeAll(returns);
					//add current sequence
					returns.addAll(cards.subList(i-count, i));
					
					//set current count to max
					max=count;
					//reset count to 0
					count=0;
				}
			}
			//set the previous card to be the current one and increment i
			prevCard = thisCard;
			i++;
		}	
			
		return returns;
	}
	

		
		

		
		
		
		/**this method looks for card values that are the same or similar (a difference of 1 or 2). 
		 * It calculates the difference between two consecutive values of a pre-sorted array (the whole array is passed 
		 * at the moment). Its used in the other methods.
		 * 
		 * @param values is the array containing the values
		 * @param index is the position in the array containing the second value
		 * @param type specifies which match to do (same, 1, 1 or 2)
		 * @return
		 */
		private boolean matchCriteria(int value1, int value2, int type){
			boolean match = false;
			int diff=value2-value1;
			
			//check if values differ by 1
			if(type==1){
				if (diff==1){
					match=true;
				}
			}
			
			//check if values differ by 1 or 2
			else if(type==2){
				if (diff==1 || diff==2){
					match=true;
				}
			}
			
			//check if values are the same
			else if(type==0){
				if(diff==0){
					match=true;
				}
			}
			
			//handle other type values
			else{
				throw new IllegalArgumentException("that is not a valid type");
			}
			return match;
		}
		
		
	
}

/**
 * 
 */
package interfaces;

/**
 * @author eleanormann
 * adapted from @author Owen Astrachan, Object-Oriented Design Concepts via Playing Cards 
 * http://www.cs.duke.edu/csed/ap/cards/cardstuff.pdf
 * Removed constants from interface, mostly because I didn't use them; partly because
 * this was looking like a constants interface (antipattern), masked only by including the
 * getters in the interface.  
 * 
 */
public interface Card {

	public int getSuit();
	public int getValue();
			
}

import java.awt.Point;

/**
 * Creates a foundation that keeps track of hand size and position
 * 
 * @author Michelle Pokrass
 * 
 */
public class Foundation extends Hand
{

	/**
	 * Creates a new foundation with a given position and the card sizes
	 * 
	 * @param x
	 *            the integer x-coordinate of the foundation
	 * @param y
	 *            the integer y-coordinate of the foundation
	 */
	Foundation(int x, int y)
	{
		super(x, y, Card.WIDTH, Card.HEIGHT);
	}

	/**
	 * Checks if the item can be placed on this foundation
	 * 
	 * @param item
	 *            the object being checked
	 * @return true if the object can be placed on this foundation (it is a card
	 *         that is the same suit and one rank greater than the top card),
	 *         false otherwise
	 */
	public boolean canPlace(Movable item)
	{
		// If the item is not a card, return false
		if (!(item instanceof Card))
		{
			return false;
		}
		// Cast the item as a card
		Card card = (Card) item;
		// If this foundation is empty, and the card is an ace, return true
		if (this.hand.size() == 0)
		{
			if (card.getRank() == 1)
				return true;
			else
				return false;
		}
		else
		{
			// Check if the suit is the same and the rank of the card is one
			// greater than that of the top card
			boolean correctSuit = this.getTopCard().getSuit() == card.getSuit();
			boolean correctRank = this.getTopCard().getRank() + 1 == card
					.getRank();
			if (correctSuit && correctRank)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

	}

	/**
	 * Adds the item to this foundation
	 * 
	 * @param the
	 *            Movable item to be placed
	 */
	public void place(Movable item)
	{
		Card card = (Card) item;
		this.add(card);
	}

	/**
	 * Returns false, no cards can be picked up
	 */
	public boolean canPickUp(Point point)
	{
		return false;
	}

	/**
	 * Picks up a card from this foundation
	 * 
	 * @param point
	 *            the Point on this foundation
	 * @return returns the top card
	 */
	public Movable pickUp(Point point)
	{
		Card card = this.remove(getNoOfCards() - 1);
		return card;
	}

	/**
	 * Adds the specified card to this foundation, setting its location to this
	 * location
	 * 
	 * @param the
	 *            Card to add
	 */
	public void add(Card card)
	{

		card.setLocation(this.x, this.y);
		hand.add(card);

	}

	/**
	 * Checks if this foundation is empty
	 * 
	 * @return true if this foundation contains no cards, false otherwise
	 */
	public boolean isEmpty()
	{
		return (this.getNoOfCards() == 0);
	}

	/**
	 * Returns the suit of this foundation
	 * 
	 * @return returns the integer suit of this foundation
	 */
	public int getSuit()
	{
		return this.getTopCard().getSuit();
	}

}

import java.awt.Point;

/**
 * Creates a freecell that contains cards and keeps track of their position and
 * size
 * 
 * @author Michelle Pokrass
 * 
 */
public class Freecell extends Hand
{
	/**
	 * Creates a new freecell with the size of a card
	 * 
	 * @param x
	 *            the x-coordinate of the freecell
	 * @param y
	 *            the y-coordinate of the freecell
	 */
	Freecell(int x, int y)
	{
		super(x, y, Card.WIDTH, Card.HEIGHT);
	}

	/**
	 * Checks if the item can be placed on this freecell
	 * 
	 * @param item
	 *            the Movable item being placed
	 * @return true if the item is a card and this freecell is empty, false
	 *         otherwise
	 * 
	 */
	public boolean canPlace(Movable item)
	{

		if ((item instanceof Card) && (this.hand.size() == 0))
		{

			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Adds a card to this freecell
	 * 
	 * @param item
	 *            the item being placed
	 */
	public void place(Movable item)
	{
		Card card = (Card) item;
		this.add(card);

	}

	/**
	 * Checks if the card containing the point can be picked up
	 * 
	 * @param point
	 *            the point being checked
	 * @return true if this freecell contains the point and is not empty, false
	 *         otherwise
	 */
	public boolean canPickUp(Point point)
	{
		if ((this.contains(point)) && (!(this.isEmpty())))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Removes the card containing this point
	 * 
	 * @param point
	 *            the point that is on the card
	 * @return the Card that has been removed
	 */
	public Movable pickUp(Point point)
	{
		Card card = this.getTopCard();
		this.remove(0);
		return card;
	}

	/**
	 * Checks if this freecell is empty
	 * 
	 * @return true if the size is zero, false otherwise
	 */
	public boolean isEmpty()
	{
		return (this.getNoOfCards() == 0);
	}
}

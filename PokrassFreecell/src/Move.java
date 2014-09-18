import java.util.LinkedList;

/**
 * Creates a move object that can keep track of the movable object moving, its
 * source, and its destination
 * 
 * @author Michelle Pokrass
 * 
 */
public class Move
{
	// Creates a list of moves and variables for the object, source, and
	// destination
	private Movable object;

	private Hand sourceHand;

	private Hand destHand;

	private static LinkedList<Move> moves = new LinkedList<Move>();

	/**
	 * Calls the other constructor with null as every parameter
	 */
	public Move()
	{
		this(null, null, null);
	}

	/**
	 * Creates a new move with the given parameters and adds it to the list of
	 * moves
	 * 
	 * @param movObject
	 *            the movable object that moved
	 * @param source
	 *            the hand the object moved from
	 * @param destination
	 *            the hand the object moved to
	 */
	public Move(Movable movObject, Hand source, Hand destination)
	{

		object = movObject;
		sourceHand = source;
		destHand = destination;
		moves.add(this);

	}

	/**
	 * Deletes the last move in the list of moves
	 */
	public static void undo()
	{
		moves.removeLast();

	}

	/**
	 * Returns the last object to have moved
	 * 
	 * @return the movable object that moved last
	 */
	public static Movable getLastObject()
	{
		return moves.getLast().object;
	}

	/**
	 * Returns the hand the last object moved from
	 * 
	 * @return the last source hand
	 */
	public static Hand getLastSource()
	{
		return moves.getLast().sourceHand;
	}

	/**
	 * REturns the hand the last object moved to
	 * 
	 * @return the last destination hand
	 */
	public static Hand getLastDest()
	{
		return moves.getLast().destHand;
	}

	/**
	 * Creates a string for the last move
	 * 
	 * @return returns the String representation of the last move
	 */
	public String toString()
	{
		return String.format("Moved the card(s) : %s to: %s from: %s",
				getLastObject(), getLastDest(), getLastSource());
	}
}

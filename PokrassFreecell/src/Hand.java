import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * Creates a hand (collection of cards) that keeps track of the amount of cards,
 * their position, and other details
 * 
 * @author Michelle Pokrass
 * 
 */
public abstract class Hand extends Movable
{
	// Create a list of cards
	protected ArrayList<Card> hand;

	/**
	 * Calls the other constructor with 0 as every parameter
	 */
	public Hand()
	{
		this(0, 0, 0, 0);
	}

	/**
	 * Creates a new hand of cards
	 * 
	 * @param x
	 *            the integer x-coordinate of the hand
	 * @param y
	 *            the integer y-coordinate of the hand
	 * @param width
	 *            the integer width of the hand
	 * @param height
	 *            the integer height of the hand
	 */
	public Hand(int x, int y, int width, int height)
	{
		// Creates a movable object with the parameters that have been passed
		// through
		super(x, y, width, height);
		// Create the actual list of cards
		hand = new ArrayList<Card>();
	}

	/**
	 * Creates a new hand of cards
	 * 
	 * @param str
	 *            the String list of cards, e.g. ("JS TH 3C 7D")
	 */
	public Hand(String str)
	{
		// Calls the other constructor with 0 as every parameter
		this(0, 0, 0, 0);
		// Create the actual list of cards
		hand = new ArrayList<Card>();
		// Create a new string tokenizer, and create a card and add it for every
		// token
		StringTokenizer st = new StringTokenizer(str, " ");
		while (st.hasMoreTokens())
		{
			hand.add(new Card(st.nextToken()));
		}

	}

	/**
	 * Sets the location of the card and adds it to this hand
	 * 
	 * @param card
	 *            the Card to add
	 */
	public void add(Card card)
	{
		card.setLocation(x, y);
		hand.add(card);
	}

	/**
	 * Returns the size of the hand
	 * 
	 * @return returns the integer amount of cards in the hand
	 */
	public int getNoOfCards()
	{
		return hand.size();
	}

	/**
	 * Removes a card from the hand
	 * 
	 * @param card
	 *            the Card to remove
	 * @return true if the hand contained the card, false if not
	 */
	public boolean remove(Card card)
	{
		return hand.remove(card);
	}

	/**
	 * Returns the top card of the hand
	 * 
	 * @return the last Card in the hand, or null if there are no cards
	 */
	public Card getTopCard()
	{
		if (hand.size() == 0)
			return null;
		return hand.get(hand.size() - 1);
	}

	/**
	 * Returns the bottom card of the hand
	 * 
	 * @return the first Card of the hand or null if it is empty
	 */
	public Card getBottomCard()
	{
		if (hand.size() == 0)
			return null;
		return hand.get(0);
	}

	/**
	 * Removes the card in the specified index
	 * 
	 * @param index
	 *            the integer index to remove
	 * @return the Card that has been removed
	 */
	public Card remove(int index)
	{
		return hand.remove(index);
	}

	/**
	 * Clears the hand, removing all cards
	 */
	public void clear()
	{
		while (!(hand.isEmpty()))
		{
			hand.remove(0);
		}
	}

	/**
	 * Creates a String representation of the hand
	 * 
	 * @return returns the String representation of the hand e.g.
	 *         ("JS TH 3C 7D")
	 */
	public String toString()
	{
		String handString = "";
		// Go through every card in the hand, adding it to the string
		for (int card = 0; card < hand.size(); card++)
		{
			if (card == 0)
				handString = "" + hand.get(card);
			else
				handString = handString + " " + hand.get(card);
		}
		return handString;
	}

	/**
	 * Overrides the draw method, draws every card within the hand
	 */
	public void draw(Graphics g)
	{
		// Draws a black rectangle around the first card
		g.setColor(Color.BLACK);
		g.draw3DRect(x + 1, y + 1, Card.WIDTH - 2, Card.HEIGHT - 2, true);
		for (Card next : hand)
			next.draw(g);

	}

	/**
	 * Overrides the move method, moving every card within the hand
	 */
	public void move(Point initialPos, Point finalPos)
	{
		super.move(initialPos, finalPos);

		// Move each card in the hand together with the actual hand
		for (Card card : hand)
		{
			card.move(initialPos, finalPos);
		}
	}

	/**
	 * Returns the card in the given index
	 * 
	 * @param index
	 *            the integer index of the card
	 * @return the Card at the given index
	 */
	public Card getCard(int index)
	{
		return hand.get(index);
	}

	/**
	 * Returns an object
	 * 
	 * @param object
	 *            the movable object to pick up
	 * @return the movable object that has been picked up
	 */
	public Movable pickUp(Movable object)
	{
		// Calls the same pickup using the objects location
		return pickUp(object.getLocation());
	}

	public abstract boolean canPlace(Movable item);

	public abstract void place(Movable item);

	public abstract boolean canPickUp(Point point);

	public abstract Movable pickUp(Point point);

}

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Creates a new hand of cards that keeps track of placement, the cards it
 * contains, their positions, and other attributes
 * 
 * @author Michelle Pokrass
 * 
 */
public class Cascade extends Hand
{

	/**
	 * Creates a new cascade with the given position and card size
	 * 
	 * @param x
	 *            the integer x-coordinate of the cascade
	 * @param y
	 *            the integer y-coordinate of the cascade
	 */
	Cascade(int x, int y)
	{
		super(x, y, Card.WIDTH, Card.HEIGHT);
	}

	/**
	 * Checks if the item can be placed on this cascade
	 * 
	 * @return true if the item to be placed is a proper cascade and is placed
	 *         on a card that allows it to remain a cascade, false otherwise
	 */
	public boolean canPlace(Movable item)
	{
		// If this cascade is empty, return true
		if (this.hand.size() == 0)
		{
			return true;
		}
		// If the item is a card, check if its rank is one less than the top
		// card rank and its suit is a different colour. If so, return true
		if (item instanceof Card)
		{
			Card topCard = this.getTopCard();
			Card cardToPlace = (Card) item;
			if ((cardToPlace.getSuit() % 2 != topCard.getSuit() % 2)
					&& (cardToPlace.getRank() + 1 == topCard.getRank()))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		// If the item is a hand, get its bottom card and perform the same
		// check. Return true if its rank is one less than the top card rank and
		// its suit is a different colour
		else if (item instanceof Hand)
		{
			Card topCard = this.getTopCard();
			Hand handToPlace = (Hand) item;
			Card bottomCard = handToPlace.getBottomCard();
			if ((bottomCard.getSuit() % 2 != topCard.getSuit() % 2)
					&& (bottomCard.getRank() + 1 == topCard.getRank()))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}

	}

	/**
	 * Adds the item to this cascade
	 * 
	 * @param item
	 *            the movable to be placed
	 */
	public void place(Movable item)
	{
		// If the item is a card, cast it as such and add it to this cascade
		if (item instanceof Card)
		{
			Card cardToPlace = (Card) item;
			this.add(cardToPlace);

		}
		// If the item is a hand, cast it as such and add it to the cascade, one
		// card at a time
		else if (item instanceof Hand)
		{
			Hand handToPlace = (Hand) item;
			for (int index = 0; index < handToPlace.getNoOfCards(); index++)
			{
				this.add(handToPlace.getCard(index));
			}

		}

	}

	/**
	 * Checks if the cards containing the point can be picked up
	 * 
	 * @param point
	 *            the point that is in the cards to be checked
	 * @return true if the point is only in the top card or the cards containing
	 *         the point form a proper cascade (alternating colour, decreasing
	 *         rank), false otherwise
	 */
	public boolean canPickUp(Point point)
	{
		// If this cascade contains the point, find the first card (from the
		// top) that contains the point
		if (this.contains(point))
		{
			for (int index = this.getNoOfCards() - 1; index >= 0; index--)
			{
				if (this.getCard(index).contains(point))
				{
					// If the top card contains the point, return true
					if (index == this.getNoOfCards() - 1)
					{
						return true;
					}
					// Check from the first card that contains the point, to the
					// top of the cascade
					else
					{
						for (int index2 = index; index2 < this.getNoOfCards() - 1; index2++)
						{
							// Check that the colours are alternating
							boolean correctSuit = this.getCard(index2)
									.getSuit() % 2 != this.getCard(index2 + 1)
									.getSuit() % 2;
							// Check that the rank is one greater than the next
							// rank
							boolean correctRank = this.getCard(index2)
									.getRank() - 1 == this.getCard(index2 + 1)
									.getRank();

							if (!(correctSuit && correctRank))
							{
								return false;
							}
						}
						return true;
					}
				}

			}
			return false;
		}

		else
			return false;

	}

	/**
	 * Picks up the cards containing the point
	 * 
	 * @param point
	 *            the point that is in the object to be picked up
	 * @return the movable object that contains the point
	 */
	public Movable pickUp(Point point)
	{
		// Check if this can be picked up
		if (this.canPickUp(point))
		{
			// Go through every card (from the top) to find the card that first
			// contains the point
			for (int index = hand.size() - 1; index >= 0; index--)
			{
				Card card = this.getCard(index);
				if (card.contains(point))
				{
					// If it is one card, remove and return it
					if (index == hand.size() - 1)
					{
						this.remove(index);
						return card;
					}
					else
					{
						// Create a new cascade
						Hand newCascade = new Cascade(card.x, card.y);
						int originalSize = this.hand.size();
						// Run this loop as many times as there are cards that
						// must be removed
						for (int index2 = index; index2 < originalSize; index2++)
						{
							// Remove the card and add it to the new cascade
							Card removed = this.remove(index);
							newCascade.add(removed);

						}
						// Return the new cascade
						return newCascade;
					}

				}

			}
		}
		return null;
	}

	/**
	 * Adds a card to this cascade, changing the size
	 * 
	 * @param card
	 *            the card to be added
	 */
	public void add(Card card)
	{
		int yPos;
		// If this cascade is empty, set the y position of this card to the
		// cascade y position
		if (this.isEmpty())
			yPos = this.y;
		// Set the y position of this card to a quarter of a card greater than
		// the last card in this cascade
		else
			yPos = this.getTopCard().y + (int) (.25 * Card.HEIGHT);
		// Set the new location of the card
		card.setLocation(x, yPos);
		// Correct the height of the card
		this.height = Card.HEIGHT
				+ (int) (this.getNoOfCards() * (0.25) * (Card.HEIGHT));
		// Add the card to this cascade
		hand.add(card);
	}

	/**
	 * Removes a card from this cascade, changes the size
	 * 
	 * @param card
	 *            the card to be removed
	 * @return true if this cascade contained the hand, false otherwise
	 */
	public boolean remove(Card card)
	{
		// Set this height to the card height if it will be empty after the
		// remove
		if (this.getNoOfCards() == 1)
			this.height = Card.HEIGHT;
		// Modify the height to the correct size based on the size of the
		// cascade
		else
			this.height = Card.HEIGHT
					+ (int) ((this.getNoOfCards() - 2) * (0.25) * (Card.HEIGHT));
		// Remove the card from this cascade
		return hand.remove(card);
	}

	/**
	 * Removes the card at the specified index
	 * 
	 * @param index
	 *            the integer index of the card to remove
	 * @return returns the card that was removed
	 */
	public Card remove(int index)
	{
		// If the cascade is empty, set its height to the card height
		if (this.isEmpty())
			this.height = Card.HEIGHT;
		// Otherwise, subtract a quarter card from the height
		else
			this.height -= (0.25 * Card.HEIGHT);
		// Remove the card at the specified index
		return hand.remove(index);

	}

	/**
	 * Checks if this cascade is empty
	 * 
	 * @return true if the amount of cards is zero, false otherwise
	 */
	public boolean isEmpty()
	{
		return (this.getNoOfCards() == 0);
	}

	/**
	 * Removes the movable item from this cascade
	 * 
	 * @param item
	 *            the movable item to be removed
	 * @return returns the movable item that has been removed
	 */
	public Movable pickUp(Movable item)
	{
		// If the item is a card, cast it and remove it
		if (item instanceof Card)
		{
			Card card = (Card) item;
			this.remove(card);
			return card;
		}
		// If the item is a cascade, cast it and remove every card in order
		else
		{
			Hand cascade = (Cascade) item;
			for (int index = 0; index < cascade.getNoOfCards(); index++)
			{
				this.remove(cascade.getCard(index));
			}
			return cascade;
		}

	}

	/**
	 * Draws this cascade
	 */
	public void draw(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.draw3DRect(x + 1, y + 1, Card.WIDTH - 2, Card.HEIGHT - 2, true);
		for (Card next : hand)
			next.draw(g);

	}
}

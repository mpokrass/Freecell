import java.util.Comparator;
import java.awt.*;
import javax.swing.*;

/**
 * Card class creates card objects that keep track of rank, suit, image,
 * placement, size, and face up/down
 * 
 * @author Michelle Pokrass
 * 
 */
public class Card extends Movable implements Comparable<Card>
{
	private int rank; // A -1, 2 - 10, J Q K

	private int suit; // D - 1, C - 2, H - 3, S - 4

	private boolean isFaceUp;

	private static Image backgroundImage = new ImageIcon("images\\redback.png")
			.getImage();

	public static final int WIDTH = backgroundImage.getWidth(null);

	public static final int HEIGHT = backgroundImage.getHeight(null);

	private Image image;

	/**
	 * Creates a new card and sets it rank, suit, image, and size
	 * 
	 * @param rank
	 *            the integer rank of the card
	 * @param suit
	 *            the integer suit of the card
	 */
	public Card(int rank, int suit)
	{
		super(0, 0, 0, 0);
		this.rank = rank;
		this.suit = suit;
		isFaceUp = false;
		// Get the unique image file of the card
		String imageFile = "images\\" + " dchs".charAt(suit) + rank + ".png";
		image = new ImageIcon(imageFile).getImage();
		// Set the size of the card based on the image size
		setSize(image.getWidth(null), image.getHeight(null));
	}

	/**
	 * Calls the other constructor with the rank and suit as integers
	 * 
	 * @param str
	 *            the card value, and suit in string form, for example "AD" or
	 *            "4S"
	 */
	public Card(String str)
	{
		this(" A23456789TJQK".indexOf(str.charAt(0)), " DCHS".indexOf(str
				.charAt(1)));
	}

	/**
	 * Returns the integer rank of the card
	 * 
	 * @return the integer rank of the card
	 */
	public int getRank()
	{
		return rank;
	}

	/**
	 * Returns the integer suit of the card
	 * 
	 * @return the integer suit of the card
	 */
	public int getSuit()
	{
		return suit;
	}

	/**
	 * Return if the card is face up or face down
	 * 
	 * @return true if the card is face up, false if it is face down
	 */
	public boolean isFaceUp()
	{
		return isFaceUp;
	}

	/**
	 * Returns true if the parameter equals this card
	 * 
	 * @param other
	 *            the object to be checked
	 * @return true if the object is the same as this card (same rank and suit)
	 */
	public boolean equals(Object other)
	{
		// If the object is not a card, return false
		if (!(other instanceof Card))
			return false;
		// Cast the object as a card
		Card otherCard = (Card) other;
		// Return if the ranks and suits are equal
		return ((this.rank == otherCard.rank) && (this.suit == otherCard.suit));

	}

	/**
	 * Flip the card over; if it is face down put it face up and if it is face
	 * up put it face down
	 */
	public void flip()
	{
		isFaceUp = !isFaceUp;
	}

	/**
	 * Compare this card to the card parameter
	 * 
	 * @param card
	 *            the Card to compare to this card
	 * @return if the two cards are the same suit, return the difference between
	 *         their ranks. Otherwise return the difference between their suits.
	 */
	public int compareTo(Card card)
	{
		if (this.suit - card.suit == 0)
			return (this.rank - card.rank);
		else
			return (this.suit - card.suit);
	}

	/**
	 * Returns this card in String form
	 * 
	 * @return the string representation of the card in "RANKSUIT" form, for
	 *         example "4D"
	 */
	public String toString()
	{
		return String.format("%s%s", "XA23456789TJQK".charAt(this.rank),
				"YDCHS".charAt(this.suit));
	}

	/**
	 * Check if this card can be placed on the foundation
	 * 
	 * @param card
	 *            the top card of the foundation
	 * @return true if it can be placed (same suit and one rank greater), false
	 *         if it cannot be placed
	 */
	public boolean canPlaceFoundation(Card card)
	{
		if ((this.suit == card.suit) && (this.rank + 1 == card.rank))
			return true;
		else
			return false;
	}

	/**
	 * Check if this card can be placed on the cascade
	 * 
	 * @param card
	 *            the top card of the cascade
	 * @return true if this card can be placed on the cascade (it is a different
	 *         colour and its rank is one less than the cascade), false if it
	 *         cannot be placed
	 */
	public boolean canPlaceCascade(Card card)
	{
		if ((this.suit % 2 != card.suit % 2) && (this.rank - 1 == card.rank))
			return true;
		else
			return false;
	}

	/**
	 * Draws a card in a Graphics context
	 * 
	 * @param g
	 *            Graphics to draw the card in
	 */
	public void draw(Graphics g)
	{
		if (isFaceUp)
			g.drawImage(image, x, y, null);
		else
			g.drawImage(backgroundImage, x, y, null);
	}

	/**
	 * A comparator that compares using rank order instead of suit order
	 */
	public static final Comparator<Card> RANK_ORDER = new Comparator<Card>() {
		/**
		 * 
		 * @param first
		 *            the first card to compare
		 * @param second
		 *            the second care to compare
		 * @return if this first card has a greater rank return a number > 0, 0
		 *         if they have the same rank and a number < 0 if the second
		 *         number is greater than the first
		 */
		public int compare(Card first, Card second)
		{

			return (first.rank - second.rank);
		}
	};

}

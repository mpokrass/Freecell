import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Creates a list of 52 cards that emulates a standard deck. Keeps track of the
 * top card, order, etc.
 * 
 * @author Michelle Pokrass
 * 
 */
public class Deck
{
	private Card[] deck;

	private int topCard;

	/**
	 * Creates a new array with 52 cards, adds 13 cards of each suit, with
	 * proper ranks
	 */
	public Deck()
	{
		deck = new Card[52];
		topCard = 0;
		// Go through each suit
		for (int suit = 1; suit <= 4; suit++)
		{
			// Go through each rank
			for (int rank = 1; rank <= 13; rank++)
			{
				deck[topCard++] = new Card(rank, suit);
			}
		}

	}

	/**
	 * Returns the top card of the deck, moving the top card down one
	 * 
	 * @return the top Card of the deck
	 */
	public Card dealACard()
	{

		return deck[--topCard];
	}

	/**
	 * Moves all the cards in the deck to a random position within it
	 */
	public void shuffle()
	{
		// Create a new deck and a new random class
		Card[] deck2 = new Card[52];
		Random rndm = new Random();
		// Find a random unfilled space in the new deck for every card
		for (int card = 0; card < 52; card++)
		{
			int randomPosition = rndm.nextInt(52);
			while (deck2[randomPosition] != null)
			{
				randomPosition = rndm.nextInt(52);
			}
			// Copy the card to the new deck
			deck2[randomPosition] = deck[card];
		}
		// Save the new deck as the old one
		deck = deck2;
		// Reset the top card
		topCard = deck.length;
	}

	/**
	 * Returns the amount of cards left
	 * 
	 * @return the integer amount of cards left
	 */
	public int cardsLeft()
	{
		return topCard;
	}

	/**
	 * Creates a String representation of the deck, with every card
	 * 
	 * @return returns the string deck
	 */
	public String toString()
	{
		String d = "" + deck[0];
		for (int card = 1; card < 52; card++)
		{
			d = d + " " + deck[card];
		}
		return d;

	}

	/**
	 * Returns the card at the index
	 * 
	 * @param index
	 *            the integer index of the card
	 * @return the Card at the index
	 */
	public Card getCard(int index)
	{
		return deck[index];
	}

}

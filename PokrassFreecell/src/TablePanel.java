import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Creates a table panel that draws all of the cards, keeps track of user input
 * and checks for wins, autocompletion, etc.
 * 
 * @author Michelle Pokrass
 * 
 */
public class TablePanel extends JPanel
{
	// Create all of the variables for sizes of cascades, freecells, and
	// foundations
	private final int CASCADE_X = 120;

	private final int CASCADE_Y = 150;

	private final int CASCADE_SPACE = 95;

	private final int FREECELL_X = 75;

	private final int FREECELL_Y = 20;

	private final int FREECELL_SPACE = 95;

	private final int FOUNDATION_X = 550;

	private final int FOUNDATION_Y = 20;

	private final int FOUNDATION_SPACE = 95;

	// Create a frame, deck, arrays for all of the hands
	private JFrame parentFrame;

	private Deck myDeck;

	private Hand[] cascades;

	private Hand[] freecells;

	private Hand[] foundations;

	private ArrayList<Hand> allHands;

	// Create variables for the objects involved in moves (movable, source and
	// destination hand)
	private Movable selectedItem;

	private Card movingCard;

	private Hand sourceHand;

	private Point lastPoint;

	final static int ANIMATION_FRAMES = 8;

	private boolean animate = true;

	// Create variables to keep track of moves, if the game has been one, the
	// delay in animation
	private Move move;

	private boolean win;

	private boolean autoComplete;

	private Card toAutoPlace;

	private int delay = 0;

	/**
	 * Creates a new panel
	 * 
	 * @param parentFrame
	 *            the Jframe the panel is contained in
	 */
	public TablePanel(JFrame parentFrame)
	{
		// Set up the size and background colour
		setPreferredSize(new Dimension(1000, 600));
		this.setBackground(new Color(125, 0, 75));
		this.parentFrame = parentFrame;

		// Set up the deck, cascades, foundations and freecells
		myDeck = new Deck();
		allHands = new ArrayList<Hand>();

		cascades = new Cascade[8];
		int xCascade = CASCADE_X;
		int yCascade = CASCADE_Y;
		for (int i = 0; i < cascades.length; i++)
		{
			cascades[i] = new Cascade(xCascade, yCascade);
			allHands.add(cascades[i]);
			xCascade += CASCADE_SPACE;
		}

		freecells = new Freecell[4];
		int xFreecell = FREECELL_X;
		int yFreecell = FREECELL_Y;
		for (int i = 0; i < freecells.length; i++)
		{
			freecells[i] = new Freecell(xFreecell, yFreecell);
			allHands.add(freecells[i]);
			xFreecell += FREECELL_SPACE;
		}

		foundations = new Foundation[4];
		int xFoundation = FOUNDATION_X;
		int yFoundation = FOUNDATION_Y;
		for (int i = 0; i < foundations.length; i++)
		{
			foundations[i] = new Foundation(xFoundation, yFoundation);
			allHands.add(foundations[i]);
			xFoundation += FOUNDATION_SPACE;
		}

		// Add mouse listeners to the table panel
		this.addMouseListener(new MouseHandler());
		this.addMouseMotionListener(new MouseMotionHandler());
		// Set default win to false and autocomplete to true
		win = false;
		autoComplete = true;
	}

	/**
	 * Restart the game, clearing all essential variables
	 */
	public void newGame()
	{
		// Create a new instance of the move class
		move = new Move();
		// Clear all the foundations, freecel
		for (Hand hand : allHands)
		{
			hand.clear();
		}
		// Create a new deck and shuffle it
		myDeck = new Deck();
		myDeck.shuffle();
		delay = 0;
		// Deal all of the cards in the deck to the 8 cascades in order
		for (int index = 0; index < 52; index++)
		{
			Card newCard = myDeck.dealACard();
			cascades[index % 8].add(newCard);
			moveACard(newCard, new Point(0, 0), newCard.getLocation());
		}
		// Redraw the panel
		repaint();
		// Check if any of the cards can be moved to the foundations, if so,
		// place them
		while (canAutoComplete())
		{
			autoComplete();
		}
		// Check if the game has been one
		checkWin();
		// Redraw the panel
		repaint();

	}

	/**
	 * Turns auto complete on
	 */
	public void autoOn()
	{
		autoComplete = true;
	}

	/**
	 * Turns auto complete off
	 */
	public void autoOff()
	{
		autoComplete = false;
	}
	/**
	 * Draw the panel
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		// Draw all the hands in the panel
		for (Hand next : allHands)
		{
			next.draw(g);
		}

		// Draw selected Cascade or Card on top
		if (selectedItem != null)
			selectedItem.draw(g);
		if (movingCard != null)
			movingCard.draw(g);
	}

	// Move a card with animation
	/**
	 * Moves the card to its final position
	 * 
	 * @param cardToMove
	 *            the card to be moved
	 * @param pos
	 *            the starting point
	 * @param finalPos
	 *            the ending point
	 */
	public void moveACard(final Card cardToMove, Point pos, Point finalPos)
	{
		// Find the small increment
		int dx = (finalPos.x - pos.x) / ANIMATION_FRAMES;
		int dy = (finalPos.y - pos.y) / ANIMATION_FRAMES;
		movingCard = cardToMove;
		// For every animation frame, add the small increment and redraw
		for (int times = 1; times <= ANIMATION_FRAMES; times++)
		{
			pos.x += dx;
			pos.y += dy;
			cardToMove.setLocation(pos.x, pos.y);

			// Update the drawing area
			paintImmediately(0, 0, getWidth(), getHeight());
			delay(delay);

		}
		movingCard = null;
		if (!cardToMove.isFaceUp())
			cardToMove.flip();
		cardToMove.setLocation(finalPos);
	}

	// A simple method to delay
	/**
	 * Pauses the thread
	 * 
	 * @param msec
	 *            the integer amount of milliseconds to delay
	 */
	private void delay(int msec)
	{
		try
		{
			Thread.sleep(msec);
		}
		catch (Exception e)
		{
		}
	}

	// Inner class to handle mouse events
	/**
	 * Handles mouse clicks and releases
	 * 
	 * @author Michelle Pokrass
	 * 
	 */
	private class MouseHandler extends MouseAdapter
	{

		public void mousePressed(MouseEvent event)
		{
			Point selectedPoint = event.getPoint();

			// Pick up one of cards from a Hand
			for (Hand next : allHands)
				if (next.contains(selectedPoint))
				{
					// Split off this section or pick up a Card
					selectedItem = next.pickUp(selectedPoint);

					repaint();
					// In case our move is not valid, we want to return the
					// Card(s) to where they initially came from
					sourceHand = next;
					lastPoint = selectedPoint;
					repaint();
					return;
				}
		}

		public void mouseReleased(MouseEvent event)
		{
			if (selectedItem != null)
			{
				// Check to see if we can add this to another cascade
				// foundation or Freecell
				for (Hand next : allHands)
					if (selectedItem.intersects(next))
					{
						if (next.canPlace(selectedItem))
						{
							// If there are enough free spaces, place the item,
							// create a new move
							if (enoughFreecells(selectedItem, next))
							{
								next.place(selectedItem);
								move = new Move(selectedItem, sourceHand, next);
								// Check for autocompletion and check for wins
								while (canAutoComplete())
								{
									autoComplete();
								}
								checkWin();
								repaint();
							}
							// If there are not enough free spaces, return the
							// item to its source
							else
							{
								sourceHand.place(selectedItem);
							}
						}
						// If the item cannot be placed, return it to its source
						else
						{
							sourceHand.place(selectedItem);
						}
						selectedItem = null;
						repaint();
						return;
					}

				// Return to original spot
				sourceHand.place(selectedItem);
				selectedItem = null;

				repaint();
			}
		}
	}

	/**
	 * Undoes the last move and repaints the panel
	 */
	void undoMove()
	{
		// Get the last item moved, its destination, and its source
		selectedItem = Move.getLastObject();
		Hand newSource = Move.getLastDest();
		Hand newDest = Move.getLastSource();

		// Create a new hand for the new destination
		Hand newDestination;
		// Pick up the object from its new source
		newSource.pickUp(selectedItem);
		// Cast the new destination
		if (newDest instanceof Cascade)
		{
			newDestination = (Cascade) newDest;
		}
		else if (newDest instanceof Freecell)
		{
			newDestination = (Freecell) newDest;
		}
		else if (newDest instanceof Foundation)
		{
			newDestination = (Foundation) newDest;
		}
		else
		{
			newDestination = newDest;
		}
		// Place the picked up obkect
		newDestination.place(selectedItem);
		selectedItem = null;
		// Remove the move from the move class
		Move.undo();
		// Redraw the panel
		repaint();

	}

	/**
	 * Checks if there are enough free spaces to move the item
	 * 
	 * @param item
	 *            the item to be moved
	 * @param destination
	 *            the destination of the item
	 * @return true if there are enough free spaces, false othewise
	 */
	boolean enoughFreecells(Movable item, Hand destination)
	{
		boolean enough = false;
		// If the item is a card, return true;
		if (item instanceof Card)
		{
			return true;
		}
		// If the item is a cascade, get the amount of cards in it
		else if (item instanceof Cascade)
		{
			Hand cascade = (Cascade) item;
			int size = cascade.getNoOfCards();
			// Find the amount of empty freecells
			int freeFreecells = 0;
			for (Hand freecell : freecells)
			{
				if (freecell.isEmpty())
					freeFreecells++;
			}
			// Find the amount of empty freecells that are neight the
			// destination nor the source
			int freeCascades = 0;
			for (Hand cascade2 : cascades)
			{
				if (cascade2.isEmpty() && !(cascade2.equals(destination))
						&& !(cascade2.equals(sourceHand)))
					freeCascades++;

			}

			// The number of cards that can be moved = (1 + number of empty
			// freecells) * 2 ^
			// (number of empty cascades)
			int canBeMoved = (int) ((1 + freeFreecells) * Math.pow(2,
					freeCascades));
			// If the size of the hand is less than the max that can be moved,
			// return true
			enough = (size <= canBeMoved);
			if (!(enough))
			{
				// Create a message box that displays how many cards can be
				// moved
				JOptionPane.showMessageDialog(parentFrame, "You have "
						+ freeFreecells + " empty freecells and "
						+ freeCascades
						+ " empty cascades, so you can only move " + canBeMoved
						+ " cards. You are trying to move " + size + " cards.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			return enough;
		}
		else
		{
			return false;
		}

	}

	/**
	 * Check if the game has been won, if so display the animation and a dialog
	 * box
	 */
	void checkWin()
	{
		// Count the amount of cards in the foundations, if there are 52, the
		// game has been won
		int cardsInFound = 0;
		for (Hand foundation : foundations)
		{
			cardsInFound += foundation.getNoOfCards();
		}
		if (cardsInFound == 52)
			win = true;
		else
			win = false;
		// If the game has been won, change the delay and move all the cards to
		// a random position
		if (win)
		{
			delay = 10;
			Random rndm = new Random();

			for (int index = 51; index >= 0; index--)
				moveACard(myDeck.getCard(index), myDeck.getCard(index)
						.getLocation(),
						new Point(rndm.nextInt(910), rndm.nextInt(510)));
			// Display a dialog box stating that the game has been won
			JOptionPane.showMessageDialog(parentFrame,
					"Congratulations, You have won the game!", "You win!",
					JOptionPane.INFORMATION_MESSAGE);
		}
		repaint();
	}

	/**
	 * Checks if any cards can be moved up to the foundation
	 * 
	 * @return true if a card can be moved up, false otherwise
	 */
	boolean canAutoComplete()
	{
		// If the user does not want auto completion, return false
		if (!(autoComplete))
			return false;
		// Go through every foundation
		for (Hand foundation : foundations)
		{
			// If the foundation is empty and the top card of a cascade or
			// freecell is an
			// ace, return true and set the card to place as the ace
			if (foundation.getTopCard() == null)
			{
				for (Hand cascade : cascades)
				{
					if (cascade.isEmpty())
						continue;
					if (cascade.getTopCard().getRank() == 1)
					{
						toAutoPlace = cascade.getTopCard();
						return true;
					}
				}
				for (Hand freecell : freecells)
				{
					if (freecell.isEmpty())
						continue;
					if (freecell.getTopCard().getRank() == 1)
					{
						toAutoPlace = freecell.getTopCard();
						return true;
					}
				}
			}
			else
			{
				// Get the suit and rank of the foundation card
				int foundationSuit = foundation.getTopCard().getSuit();
				int foundationRank = foundation.getTopCard().getRank();
				// Check every cascade, skipping the empty ones
				for (Hand cascade : cascades)
				{
					if (cascade.isEmpty())
						continue;
					// Get the suit and rank of the top of the cascade
					int cascadeSuit = cascade.getTopCard().getSuit();
					int cascadeRank = cascade.getTopCard().getRank();
					// Check if the suit and rank are correct (same suit,
					// cascade rank one greater than foundation)
					boolean suit = (foundationSuit == cascadeSuit);
					boolean rank = (foundationRank + 1 == cascadeRank);
					if (suit && rank)
					{
						// Check if the 2 cards of the opposite colour with one
						// rank less are still on the board (not in the
						// foundation)
						int amount = 0;
						for (Hand foundation2 : foundations)
						{
							// Check every foundation, skipping the empty ones
							if (foundation2.isEmpty())
								continue;
							else
							{
								// Check every card in the foundation
								for (int index = 0; index < foundation2
										.getNoOfCards(); index++)
									// Check if the card is opposite colour and
									// one rank less than the cascade card
									if ((foundation2.getCard(index).getRank() + 1 == cascadeRank)
											&& (foundation2.getCard(index)
													.getSuit() % 2 != cascadeSuit % 2))
										amount++;
							}
						}
						if (amount == 2)
						{
							// If both cards are in the foundation, return true
							// and set the top of the cascade as the card to be
							// auto placed
							toAutoPlace = cascade.getTopCard();
							return true;
						}
						else
							continue;

					}

				}

				// Go through every freecell, skipping the empty ones
				for (Hand freecell : freecells)
				{
					if (freecell.isEmpty())
						continue;
					// Get the suit and rank of the freecell cards
					int freecellSuit = freecell.getTopCard().getSuit();
					int freecellRank = freecell.getTopCard().getRank();
					// Check if the suit and rank are correct (same suit,
					// freecell rank one greater than foundation)
					boolean suit = (foundationSuit == freecellSuit);
					boolean rank = (foundationRank + 1 == freecellRank);
					if (suit && rank)
					{
						// Check if the 2 cards of the opposite colour with one
						// rank less are still on the board (not in the
						// foundation)
						int amount = 0;
						// Check every foundation, skipping the empty ones
						for (Hand foundation2 : foundations)
						{
							if (foundation2.isEmpty())
								continue;
							else
							{
								// Check every card in the foundation
								for (int index = 0; index < foundation2
										.getNoOfCards(); index++)
									// Check if the card is opposite colour and
									// one rank less than the cascade card
									if ((foundation2.getCard(index).getRank() + 1 == freecellRank)
											&& (foundation2.getCard(index)
													.getSuit() % 2 != freecellSuit % 2))
										amount++;
							}
						}
						if (amount == 2)
						{
							// If both cards are in the foundation, return true
							// and set the top of the cascade as the card to be
							// auto placed
							toAutoPlace = freecell.getTopCard();
							return true;
						}
						else
							continue;
					}

				}

			}
		}
		return false;
	}

	/**
	 * Moves the identified card to its correct foundation
	 */
	void autoComplete()
	{
		// Change the delay
		delay = 30;
		// Go through every cascade, check if its top card matches the card to
		// autoplace
		for (Hand cascade : cascades)
		{
			if (cascade.getTopCard() == toAutoPlace)
			{
				// Go through every foundation
				for (Hand foundation : foundations)
				{
					// Check if the foundation is empty and the card to be
					// placed is an ace
					if ((foundation.getTopCard() == null)
							&& (toAutoPlace.getRank() == 1))
					{
						// Move the card, remove it from its position, place it
						// in its new position
						moveACard(toAutoPlace, toAutoPlace.getLocation(),
								foundation.getLocation());
						cascade.pickUp(toAutoPlace);
						foundation.place(toAutoPlace);
						// Add a new move, quit the method
						move = new Move(toAutoPlace, cascade, foundation);
						toAutoPlace = null;
						return;
					}
					// Check if the foundation is the same suit and one rank
					// less than the card to be autoplaced
					else if ((foundation.getTopCard().getSuit() == toAutoPlace
							.getSuit())
							&& (foundation.getTopCard().getRank() + 1 == toAutoPlace
									.getRank()))
					{
						// Move the card, remove it from its position, place it
						// in its new position
						moveACard(toAutoPlace, toAutoPlace.getLocation(),
								foundation.getLocation());
						cascade.pickUp(toAutoPlace);
						foundation.place(toAutoPlace);
						// Add a new move, quit the method
						move = new Move(toAutoPlace, cascade, foundation);
						toAutoPlace = null;
						return;

					}
				}

			}
		}
		// Go through every freecell, check if its top card matches the card to
		// autoplace, skip the empty ones
		for (Hand freecell : freecells)
		{
			if (freecell.isEmpty())
				continue;
			if (freecell.getTopCard() == toAutoPlace)
			{
				// Check if the foundation is empty and card to be placed is an
				// ace
				for (Hand foundation : foundations)
				{

					if ((foundation.getTopCard() == null)
							&& (toAutoPlace.getRank() == 1))
					{
						// Move the card, remove it, place it
						moveACard(toAutoPlace, toAutoPlace.getLocation(),
								foundation.getLocation());
						freecell.pickUp(toAutoPlace);
						foundation.place(toAutoPlace);
						// Create a new move, quit the method
						move = new Move(toAutoPlace, freecell, foundation);
						toAutoPlace = null;
						return;
					}
					// Check if the foundation is the same suit and one rank
					// less than the card to auto place
					else if ((foundation.getTopCard().getSuit() == toAutoPlace
							.getSuit())
							&& (foundation.getTopCard().getRank() + 1 == toAutoPlace
									.getRank()))
					{
						// Move the card, remove it, place it
						moveACard(toAutoPlace, toAutoPlace.getLocation(),
								foundation.getLocation());
						freecell.pickUp(toAutoPlace);
						foundation.place(toAutoPlace);
						// Create a new move, quit the method
						move = new Move(toAutoPlace, freecell, foundation);
						toAutoPlace = null;
						return;
					}
				}

			}
		}
		toAutoPlace = null;

	}

	// Inner Class to handle mouse movements
	/**
	 * Handles mouse moved and dragged events
	 * 
	 * @author Michelle Pokrass
	 * 
	 */
	private class MouseMotionHandler implements MouseMotionListener
	{
		public void mouseMoved(MouseEvent event)
		{
			// Set the cursor to the hand if we are on a card
			Point currentPoint = event.getPoint();
			// Count down, since higher cards are on top
			for (Hand next : allHands)
				if (next.canPickUp(currentPoint))
				{
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					return;
				}

			// Otherwise we just use the default cursor
			setCursor(Cursor.getDefaultCursor());
		}

		public void mouseDragged(MouseEvent event)
		{
			Point currentPoint = event.getPoint();

			if (selectedItem != null)
			{
				// If the moving item is a cascade, add it to all hands
				if (selectedItem instanceof Cascade)
				{

					Hand moving = (Hand) selectedItem;
					allHands.add(moving);
				}

				// We use the difference between the lastPoint and the
				// currentPoint to move the Cascade so that the position of
				// the mouse on the Cascade doesn't matter.
				// i.e. we can drag the card from any point on the card image
				selectedItem.move(lastPoint, currentPoint);
				lastPoint = currentPoint;

				repaint();
				// Remove the cascade from all hands
				if (selectedItem instanceof Cascade)
				{
					Hand moving = (Hand) selectedItem;
					allHands.remove(moving);
				}
			}
		}
	}
}

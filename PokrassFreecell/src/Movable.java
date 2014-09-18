import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Creates a movable object the keeps track of position and size
 * 
 * @author Michelle Pokrass
 * 
 */
public abstract class Movable extends Rectangle
{
	/**
	 * Creates a movable object
	 * 
	 * @param x
	 *            the integer x-coordinate of the object
	 * @param y
	 *            the integer y-coordinate of the object
	 * @param width
	 *            the integer width of the object
	 * @param heightthe
	 *            integer height of the object
	 */
	public Movable(int x, int y, int width, int height)
	{
		// Creates a rectangle with the specified parameters
		super(x, y, width, height);
	}

	/**
	 * Moves the object to a specified position
	 * 
	 * @param initialPos
	 *            the Point to move from
	 * @param finalPos
	 *            the Point to move to
	 */
	public void move(Point initialPos, Point finalPos)
	{
		this.translate(finalPos.x - initialPos.x, finalPos.y - initialPos.y);
	}

	public abstract void draw(Graphics g);

}

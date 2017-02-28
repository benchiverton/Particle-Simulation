package vectors;

public class Vector2D extends Vector
{
	
	/**
	 * Constructor to create an empty (zero) 2D vector.
	 */
	public Vector2D()
	{
		super(2);
	}
	
	/**
	 * Constructor to create a vector with given x and y coordinates.
	 * @param x
	 * @param y
	 */
	public Vector2D(double x, double y)
	{
		super(2);
		this.setComponent(0, x);
		this.setComponent(1, y);
	}
	
	/**
	 * Gets the x component of the vector.
	 * @return The x component of the vector (stored in index 0).
	 */
	public double getX()
	{
		return this.getComponent(0);
	}
	
	/**
	 * Gets the y component of the vector.
	 * @return The y component of the vector (stored in index 1).
	 */
	public double getY()
	{
		return this.getComponent(1);
	}
	
}

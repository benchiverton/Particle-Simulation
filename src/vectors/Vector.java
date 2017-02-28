package vectors;

public class Vector
{

	/**
	 * The dimension of the vector.
	 */
	private int dimension;
	
	/**
	 * Array containing the components of the vector.
	 */
	private double[] components;
	
	/**
	 * Constructor to initialize a zero vector with specified dimension.
	 * @param dim The dimension of the vector.
	 */
	public Vector(int dim)
	{
		this.dimension = dim;
		components = new double[dim];
		
		for(int i = 0; i < dim; i++)
		{
			components[i] = 0.0;
		}
	}
	/**
	 * Constructor to initialize a vector of given dimension and components.
	 * @param dim
	 * @param vals
	 */
	public Vector(int dim, double[] vals)
	{
		if(dim != vals.length)
		{
			throw new VectorException("Dimension mismatch when initializing vector. "
					+ "Tried to initialize vector with dim " + dim + " with " + vals.length + "components.");
			
		}
		
		this.dimension = dim;
		components = vals;
	}
	
	/**
	 * Gets the dimension of the vector.
	 * @return The dimension of the vector.
	 */
	public int getDimension()
	{
		return dimension;
	}
	
	/**
	 * Gets the components of the vector.
	 * @return An array containing the components of the vector.
	 */
	public double[] getComponents()
	{
		return components;
	}
	
	/**
	 * Gets a specific component of the vector.
	 * @param i The index of the component to return.
	 * @return The value of the specified component in the vector.
	 */
	public double getComponent(int i)
	{
		return components[i];
	}
	
	/**
	 * Sets a specific component of the vector.
	 * @param i The index of the component to set.
	 * @param val The value to set the component to.
	 */
	public void setComponent(int i, double val)
	{
		components[i] = val;
	}
	
	/**
	 * Adds two vectors together.
	 * @param v The vector to add.
	 * @return The sum of the two vectors.
	 */
	public Vector add(Vector v)
	{
		if(this.getDimension() != v.getDimension())
		{
			throw new VectorException("Dimension mismatch when adding vectors.");
		}
		
		double[] comps = new double[this.getDimension()];
		
		for(int i = 0; i < this.getDimension(); i++)
		{
			comps[i] = this.getComponent(i) + v.getComponent(i);
		}
		
		return new Vector(getDimension(), comps);
	}
	
	/**
	 * Subtracts two vectors.
	 * @param v The vector to subtract.
	 * @return The current vector minus vector v.
	 */
	public Vector subtract(Vector v)
	{
		return this.add(v.invert());
	}
	
	/**
	 * Returns a vector with every component inverted.
	 * For example, performing this function on the vector (3, -2, 5) will return (-3, 2, -5).
	 * @return A vector where every component is negated.
	 */
	public Vector invert()
	{
		double[] comps = new double[this.getDimension()];
		
		for(int i = 0; i < this.getDimension(); i++)
		{
			comps[i] = -this.getComponent(i);
		}
		
		return new Vector(this.getDimension(), comps);
	}
	
	/**
	 * Multiplies the vector by a scalar value.
	 * @param scalar The scalar to multiply the vector by.
	 * @return A vector which has been multiplied by the given scalar.
	 */
	public Vector multiply(double scalar)
	{
		double[] comps = new double[this.getDimension()];
		
		for(int i = 0; i < this.getDimension(); i++)
		{
			comps[i] = scalar * this.getComponent(i);
		}
		
		return new Vector(getDimension(), comps);
	}
	
	/**
	 * Divides the vector by a scalar value.
	 * @param scalar The scalar to divide the vector by.
	 * @return A vector which has been divided by the given scalar.
	 */
	public Vector divide(double scalar)
	{
		double[] comps = new double[this.getDimension()];
		
		for(int i = 0; i < this.getDimension(); i++)
		{
			comps[i] = this.getComponent(i) / scalar;
		}
		
		return new Vector(getDimension(), comps);
	}
	
	/**
	 * Gets the absolute value of the vector (distance from the origin).
	 * @return The absolute value of the vector.
	 */
	public double abs()
	{
		double sqSum = 0;
		
		for(int i = 0; i < this.getDimension(); i++)
		{
			sqSum += components[i] * components[i];
		}
		
		return Math.sqrt(sqSum);
	}
	
	/**
	 * Treating these vectors as points, find the distance between them.
	 * @param v The vector to find the distance to.
	 * @return The distance between the vectors as if they were points.
	 */
	public double distance(Vector v)
	{
		if(this.getDimension() != v.getDimension())
		{
			throw new VectorException("Dimension mismatch when finding distance between vectors.");
		}
		
		double sqSum = 0;
		
		for(int i = 0; i < this.getDimension(); i++)
		{
			sqSum += Math.pow(this.getComponent(i) - v.getComponent(i), 2);
		}
		
		return Math.sqrt(sqSum);
	}
	
	/**
	 * Treating these vectors as points, find the unit vector representing the
	 * direction from this vector to the specified vector.
	 * @param v The vector to find the direction to.
	 * @return A unit vector of the direction from this vector to the specified vector.
	 */
	public Vector direction(Vector v)
	{
		Vector difference = v.subtract(this);
		return difference.divide(difference.abs());
	}
	
	/**
	 * Randomize the components of the vector.
	 * Components will be distributed according to U~[0, 1].
	 * @return this for convenience.
	 */
	public Vector randomise()
	{
		for(int i = 0; i < this.getDimension(); i++)
		{
			this.setComponent(i, Math.random());
		}
		
		return this;
	}
	
	/**
	 * 
	 * @return
	 */
	public Vector randomiseSphere()
	{
		boolean b = false;
		while(b == false){
			double a = 0;
			for(int i = 0; i < this.getDimension(); i++)
			{
				this.setComponent(i, Math.random());
				a += Math.pow(this.getComponent(i)-0.5, 2);
			}
			a = Math.sqrt(a);
			if(a <= 0.5){
				b = true;
			}
		}
		return this;
	}
	
	/**
	 * Override of toString method.
	 * Returns vector in the form (x1, x2, x3, ..., xn).
	 */
	public String toString()
	{
		String str = "(";
		
		for(int i = 0; i < this.getDimension(); i++)
		{
			str += this.getComponent(i) + ", ";
		}
		
		str = str.trim() + ")";
		
		return str;
	}
	
	/**
	 * Checks to see if two vectors are equal.
	 * @return True if two vectors are equal, false otherwise.
	 * @param v The vector to test equality with.
	 */
	public boolean equals(Vector v)
	{
		if(this.getDimension() != v.getDimension())
		{
			return false;
		}
		
		for(int i = 0; i < this.getDimension(); i++)
		{
			if(this.getComponent(i) != v.getComponent(i))
			{
				return false;
			}
		}
		
		return true;
	}
	
}

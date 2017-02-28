package particles;

import vectors.*;

public class Particle
{

	/**
	 * The current dimension the particle is in.
	 */
	private int dimension;
	
	/**
	 * Vectors representing the position, velocity and acceleration of the particle.
	 */
	private Vector position;
	private Vector velocity;
	private Vector acceleration;
	
	/**
	 * Variable representing the mass of the particle.
	 */
	private double mass;
	
	private double radius;
	
	/**
	 * Default constructor, creates a particle of mass 1 at the origin of a 2D plane.
	 */
	public Particle()
	{
		this.dimension = 2;
		this.position = new Vector(dimension);
		this.velocity = new Vector(dimension);
		this.acceleration = new Vector(dimension);
		this.mass = 1;
		this.radius = Math.cbrt(this.mass);
	}
	
	/**
	 * Constructor to create a particle of mass 1 at the specified point.
	 * @param pos The initial position of the particle.
	 */
	public Particle(Vector pos)
	{
		this.dimension = pos.getDimension();
		this.position = pos;
		this.velocity = new Vector(dimension);
		this.acceleration = new Vector(dimension);
		this.mass = 1;
		this.radius = Math.cbrt(this.mass);
	}
	
	/**
	 * Constructor to create a particle of given mass at a specified point.
	 * @param pos The initial position of the particle.
	 * @param mass The mass of the particle.
	 */
	public Particle(Vector pos, double mass)
	{
		this.dimension = pos.getDimension();
		this.position = pos;
		this.velocity = new Vector(dimension);
		this.acceleration = new Vector(dimension);
		this.mass = mass;
		this.radius = Math.cbrt(this.mass);
	}
	/**
	 * Constructor to create a particle of given mass at a specified point with specified
	 * velocity and acceleration.
	 * @param pos The initial position of particle.
	 * @param vel The initial velocity of the particle.
	 * @param acc The initial acceleration of the particle.
	 * @param mass The mass of the particle.
	 */
	public Particle(Vector pos, Vector vel, Vector acc, double mass)
	{
		this.dimension = pos.getDimension();
		
		// Check if dimensions match for all vectors before continuing.
		if(vel.getDimension() != this.dimension || acc.getDimension() != this.dimension)
		{
			throw new VectorException("Dimension mismatch when creating a new particle.");
		}
		
		this.position = pos;
		this.velocity = vel;
		this.acceleration = acc;
		this.mass = mass;
		this.radius = Math.cbrt(this.mass);
	}
	
	
	/**
	 * Gets the position of the particle.
	 * If the particle is in 2D space, this returns a 2D vector.
	 * @return The position of the particle.
	 */
	public Vector getPosition()
	{
		/*if(this.dimension == 2)
		{
			return (Vector2D)this.position;
		} else {
			return this.position;
		}*/
		
		return this.position;
	}
	
	/**
	 * Gets the velocity of the particle.
	 * If the particle is in 2D space, this returns a 2D vector.
	 * @return The velocity of the particle.
	 */
	public Vector getVelocity()
	{
		/*if(this.dimension == 2)
		{
			return (Vector2D)this.velocity;
		} else {
			return this.velocity;
		}*/
		
		return this.velocity;
	}
	
	/**
	 * Gets the acceleration of the particle.
	 * If the particle is in 2D space, this returns a 2D vector.
	 * @return The acceleration of the particle.
	 */
	public Vector getAcceleration()
	{
		/*if(this.dimension == 2)
		{
			return (Vector2D)this.acceleration;
		} else {
			return this.acceleration;
		}*/
		
		return this.acceleration;
	}
	
	/**
	 * Gets the mass of the particle.
	 * @return The mass of the particle.
	 */
	public double getMass()
	{
		return this.mass;
	}
	
	public void increaseMass(double m){
		this.mass += m;
	}
	
	public double getRadius(){
		return this.radius;
	}
	
	/**
	 * Gets the dimension the particle is currently in.
	 * @return The dimension the particle is in.
	 */
	public int getDimension()
	{
		return this.dimension;
	}
	
	/**
	 * Sets the acceleration of the particle.
	 * @param acc The new acceleration of the particle.
	 */
	public void setAcceleration(Vector acc)
	{
		if(this.getDimension() != acc.getDimension())
		{

			throw new VectorException("Dimension mismatch when setting acceleration of particle.");
		}
		
		this.acceleration = acc;
	}
	
	/**
	 * Sets the velocity of the particle.
	 * @param vel The new velocity of the particle.
	 */
	public void setVelocity(Vector vel)
	{
		if(this.getDimension() != vel.getDimension())
		{
			throw new VectorException("Dimension mismatch when setting velocity of particle.");
		}
		
		this.velocity = vel;
	}
	
	
	/**
	 * Calculates and sets new position and velocity of particle after delta time.
	 * @param time The delta time.
	 */
	public void update(double time)
	{
		for(int i = 0; i <this.getDimension(); i++)
		{
			this.position.setComponent(i, this.position.getComponent(i) + this.velocity.getComponent(i) * time + 0.5 * this.acceleration.getComponent(i) * Math.pow(time, 2));
			this.velocity.setComponent(i, this.velocity.getComponent(i) + this.acceleration.getComponent(i) * time);
		}
	}
	
}

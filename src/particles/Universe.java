package particles;

import java.util.ArrayList;

import vectors.Vector;
import vectors.Vector2D;
import visual.Renderer;

public class Universe
{

	// Array of particles in the universe
	private ArrayList<Particle> particles;
	
	// Dimension of the universe
	private int dimension;
	
	// Time base of the universe
	private double timebase;
	
	// Elapsed time in the universe
	private double elapsedTime = 0;
	
	// Physical constants in this universe
	public static final double GRAV_CONST = 6.674E-11;
	public static final double DEF_TIMEBASE = 1;
	
	/**
	 * Constructor to create a new empty universe.
	 */
	public Universe()
	{
		this.dimension = 3;
		this.particles = new ArrayList<Particle>();
		this.timebase = DEF_TIMEBASE;
	}
	
	/**
	 * Constructor to create a new empty universe with a number of random particles.
	 * @param n The number of random particles to create.
	 * @param dim The dimension of the Universe.
	 * @param System - If you want the specific particles which 'spin' around eachother.
	 */
	public Universe(int n, int dim, boolean system)
	{
		this.dimension = dim;
		this.particles = new ArrayList<Particle>();
		this.timebase = DEF_TIMEBASE;
			
		if(system == true){
			
			double velocity = Math.sqrt(100*GRAV_CONST);
			double[] x = {0,0,0};
			double[] pos1 = {0.25,0.5,0.5};
			double[] pos2 = {0.75, 0.5, 0.5};
			double[] veloc1 = {0, velocity, 0};
			double[] veloc2 = {0, -velocity, 0};
			
			Particle A = new Particle(new Vector(3, pos1), new Vector(3, veloc1), new Vector(3, x), 100);
			Particle B = new Particle(new Vector(3, pos2), new Vector(3, veloc2), new Vector(3, x), 100);
			
			particles.add(A);
			particles.add(B);
		}
		else{
			for(int i = 0; i < n; i++){
				this.particles.add(new Particle( new Vector(dim).randomiseSphere(), Math.random()*5 ));
			}
			
		}
	}
	
	/**
	 * Calculates gravity and updates the entire universe.<p>
	 * Also checks for any collisions between particles.
	 * @param  dimension of the universe to be updated
	 */
	public void updateUniverse(int dimension)
	{
		for(int i = 0; i < this.particles.size(); i++)
		{
			Vector force = new Vector(this.dimension);
			
			for(int x = 0; x < this.particles.size(); x++)
			{
				if(x == i) continue;
				
				if(checkCollision(i, x) == true)
				{
					
					particles.get(i).setVelocity(
							particles.get(i).getVelocity().multiply(particles.get(i).getMass()).add(particles.get(x).getVelocity().multiply(particles.get(x).getMass())).divide(
									particles.get(i).getMass()+particles.get(x).getMass())
							);
					
					this.particles.get(i).increaseMass(this.particles.get(x).getMass());
					particles.remove(x);
					
					continue;
				}
				
				double gForce = GRAV_CONST * (particles.get(i).getMass() * particles.get(x).getMass()) / Math.pow((this.particles.get(i).getPosition().distance(this.particles.get(x).getPosition())), 2);
				Vector directon = particles.get(i).getPosition().direction(particles.get(x).getPosition());
				
				force = force.add(directon.multiply(gForce));
			}
			
			particles.get(i).setAcceleration(force.divide(particles.get(i).getMass()));
		}
		
		for(int i = 0; i < this.particles.size(); i++)
		{
			particles.get(i).update(timebase);
		}
		
		elapsedTime += timebase;
	}
	
	/**
	 * Checks if particles x and y have collided, within a tolerance.
	 * @param x  Particle 1
	 * @param y	 Particle 2
	 * @return   true if particles collide, false if otherwise.
	 */
	private boolean checkCollision(int x, int y){
		
		boolean b = false;
		if(Math.abs((this.particles.get(x).getPosition().distance(this.particles.get(y).getPosition()))) < 0.5*Renderer.SCALE*(this.particles.get(x).getRadius() + this.particles.get(y).getRadius()))
		{
			b = true;
		}
		return b;
	}
	
	/**
	 * Gets the elapsed time in the universe.
	 * @return The elapsed time in the universe.
	 */
	public double getElapsedTime()
	{
		return elapsedTime;
	}
	
	/**
	 * Gets an array list of all the particles in the universe.
	 * @return The array list of particles in the universe.
	 */
	public ArrayList<Particle> getParticles()
	{
		return particles;
	}
	
	/**
	 * Modifies the time base by given amount.
	 * @param amount Amount to modify time base by.
	 */
	public void modifyTimebase(double amount)
	{
		timebase += amount;
	}
	
	public double getTimeBase(){
		return timebase;
	}
	
	public static void main(String[] args)
	{
		Universe u = new Universe(1000, 3, true);
		
		while(u.getElapsedTime() < 1.0)
		{
			u.updateUniverse(2);
			System.out.println("Updated.");
		}
		
		System.out.println("Finished simulation.");
	}
	
}

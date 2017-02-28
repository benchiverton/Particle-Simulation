package visual;

import particles.Universe;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Simulation
{

	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	// The universe to simulate
	public static Universe u;

	// The window used to show the simulation
	private long window;
	
	public static int dimension = 3;
	private static boolean lighting = true;
	private static boolean system = false;
	
	public static int particleCount = 500;
	
	//Width and height of the window
	int WIDTH = 1000;
	int HEIGHT = 1000;
	
	public static void main(String[] args)
	{

		new Simulation().run();

	}

	public void run()
	{
		System.out.println("Starting simulation...");

		try
		{
			loop();

			// Destroy window and window call backs
			glfwDestroyWindow(window);

		} finally
		{
			glfwTerminate();
		}
	}

	private void loop()
	{
		Generate();
		if(dimension < 2){
			throw new SimulationError("Please enter a valid dimention.");
		}else{
			GL.createCapabilities();

			glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			
			if(lighting == true){
			
				Renderer.setupLighting(); 
				
			}

			while (glfwWindowShouldClose(window) == GLFW_FALSE)
			{
				
				Renderer.setup();
				
				for(int i = 0; i < u.getParticles().size(); i++){

					Renderer.renderParticle(i);
					
				}
				
				glEnd();
					
					///////////////////

					glfwSwapBuffers(window); // swap the color buffers

					u.updateUniverse(dimension);
					
					// Poll for window events. The key callback above will only be
					// invoked during this call.
					glfwPollEvents();
				}	
		}
	}

	private void Generate()
	{
		u = new Universe(particleCount, dimension, system);

		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (glfwInit() != GLFW_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden
													// after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be
													// resizable

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Particle Simulation", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback()
		{
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods)
			{
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, GLFW_TRUE);
				
				if(key == GLFW_KEY_P)
				{
					u.modifyTimebase(0.25);
				}
				
				if(key == GLFW_KEY_O)
				{
					u.modifyTimebase(-0.25);
				}
				
				if(key == GLFW_KEY_UP)
				{
					Renderer.increaseAngleB(0.01);
				}
				
				if(key == GLFW_KEY_DOWN)
				{
					Renderer.increaseAngleB(-0.01);
				}
				
				if(key == GLFW_KEY_RIGHT)
				{
					Renderer.increaseAngleA(0.01);
				}	
				
				if(key == GLFW_KEY_LEFT)
				{
					Renderer.increaseAngleA(-0.01);
				}
				
				if(key ==  GLFW_KEY_M)
				{
					Renderer.increaseZoom(1.01);
				}
				
				if(key ==  GLFW_KEY_N)
				{
					Renderer.increaseZoom(0.99);
				}
				
				if(key == GLFW_KEY_SPACE)
				{
					Renderer.setAngleA(Math.PI / 4);
					Renderer.setAngleB(-Math.PI / 6);
					Renderer.setZoom(1);
				}
				
				if(key == GLFW_KEY_Q && action == GLFW_RELEASE)
				{
					Renderer.switchAxis();
				}
				
				
			}
		});

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

}

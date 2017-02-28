package visual;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import vectors.Vector;

public class Renderer {
	
	//A = angle rotated RIGHT, ie - 'pushing' the z axis to the right, when the z axis is pointing to you
	private static double angleA = Math.PI / 4;
	//B = angle tilted UPWARDS, ie - 'lifting' the z axis down at the front, when the z axis is pointing to you
	private static double angleB = -Math.PI / 6;
	private static double zoom = 0.5;
	
	public static double SCALE = 0.002;
	
	private static double cosA;
	private static double sinA;
	
	private static double cosB;
	private static double sinB;
	
	private static double pointA;
	private static double pointB;
	
	private static boolean axis = true;
	private static boolean sizing = true;
	private static boolean spin = false;
	
	//Calculates the values of cosA, sinA, cosB and sinB. It then draws the axis.
	public static void setup(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glColor3f(1.0f,1.0f,1.0f);
		
		if(spin == true){
			angleA += 0.002;
		}
		
		cosA = Math.cos(angleA);
		sinA = Math.sin(angleA);
		
		cosB = Math.cos(angleB);
		sinB = Math.sin(angleB);
		
		pointA = 0.95;
		pointB = 0.05;
		
		if(axis == true){
		
		GL11.glBegin(GL_LINE_STRIP);
		glVertex3d(zoom*(-cosA),zoom*(sinB*sinA),zoom*(-cosB*sinA));
		glVertex3d(zoom*(cosA),zoom*(-sinB*sinA),zoom*(cosB*sinA));
	    glVertex3d(zoom*(pointA*cosA),zoom*(pointB*cosB-pointA*sinA*sinB),zoom*(pointB*sinB+pointA*sinA*cosB));
	    glVertex3d(zoom*(cosA),zoom*(-sinB*sinA),zoom*(cosB*sinA));
	    glVertex3d(zoom*(pointA*cosA),zoom*(-pointB*cosB-pointA*sinA*sinB),zoom*(-pointB*sinB+pointA*sinA*cosB));
	    glEnd();
	    
	    GL11.glBegin(GL_LINE_STRIP);
	    glVertex3d(0,zoom*(-cosB),zoom*(-sinB));
	    glVertex3d(0,zoom*(cosB),zoom*(sinB));
	    glVertex3d(zoom*(pointB*cosA),zoom*(pointA*cosB-pointB*sinA*sinB),zoom*(pointA*sinB+pointB*sinA*cosB));
	    glVertex3d(0,zoom*(cosB),zoom*(sinB));
	    glVertex3d(zoom*(-pointB*cosA),zoom*(pointA*cosB+pointB*sinA*sinB),zoom*(pointA*sinB-pointB*sinA*cosB));
	    glEnd();
	    
	    GL11.glBegin(GL_LINE_STRIP);
	    glVertex3d(zoom*(-sinA),zoom*(-sinB*cosA),zoom*(cosA*cosB));
	    glVertex3d(zoom*(sinA),zoom*(sinB*cosA),zoom*(-cosA*cosB));
	    glVertex3d(zoom*(pointA*sinA),zoom*(pointB*cosB+pointA*cosA*sinB),zoom*(pointB*sinB-pointA*cosA*cosB));
	    glVertex3d(zoom*(sinA),zoom*(sinB*cosA),zoom*(-cosA*cosB));
	    glVertex3d(zoom*(pointA*sinA),zoom*(-pointB*cosB+pointA*cosA*sinB),zoom*(-pointB*sinB-pointA*cosA*cosB));
		glEnd();
		}
	}
	
	/**
	 * Renders each particle in a universe as a cube at it's position.
	 * 
	 * @param i   The size of the universe
	 */
	public static void renderParticle(int i){
		double x = (Simulation.u.getParticles().get(i).getPosition().getComponent(0)-0.5)*2;
		double y = (Simulation.u.getParticles().get(i).getPosition().getComponent(1)-0.5)*2;
		double z = 0;
		if(Simulation.dimension>2){
			z = (Simulation.u.getParticles().get(i).getPosition().getComponent(2)-0.5)*2;
		}
		
		double scale = SCALE;
		
		double[] viewpointXYZ = {-sinA*cosB/zoom, -sinB/zoom, -cosA*cosB/zoom};
		Vector viewpoint = new Vector(3, viewpointXYZ);
		
		double[] positionXYZ = {x,y,z};
		Vector position = new Vector(3, positionXYZ);
		double distFromVP = position.distance(viewpoint);
		
		glColor3f(1.0f, 1.0f, 1.0f);
			
		if(sizing==true){
			scale = scale * Math.cbrt(Simulation.u.getParticles().get(i).getMass() / distFromVP);
		}
		
		double xZoomP = x * zoom + scale;
		double xZoomM = x * zoom - scale;
		double yZoomP = y * zoom + scale;
		double yZoomM = y * zoom - scale;
		double zZoomP = z * zoom + scale;
		double zZoomM = z * zoom - scale;
		
		double xApp = xZoomP*cosA - zZoomP*sinA;
		double xApm = xZoomP*cosA - zZoomM*sinA;
		double xAmp = xZoomM*cosA - zZoomP*sinA;
		double xAmm = xZoomM*cosA - zZoomM*sinA;
		double zApp = xZoomP*sinA + zZoomP*cosA;
		double zApm = xZoomP*sinA + zZoomM*cosA;
		double zAmp = xZoomM*sinA + zZoomP*cosA;
		double zAmm = xZoomM*sinA + zZoomM*cosA;

		glBegin(GL_QUADS);
		//back
		glVertex3d(xApm, yZoomP*cosB-zApm*sinB, yZoomP*sinB+zApm*cosB);
		glVertex3d(xApm, yZoomM*cosB-zApm*sinB, yZoomM*sinB+zApm*cosB);
		glVertex3d(xAmm, yZoomM*cosB-zAmm*sinB, yZoomM*sinB+zAmm*cosB);
		glVertex3d(xAmm, yZoomP*cosB-zAmm*sinB, yZoomP*sinB+zAmm*cosB);
		//bottom
		glVertex3d(xApp, yZoomM*cosB-zApp*sinB, yZoomM*sinB+zApp*cosB);
		glVertex3d(xApm, yZoomM*cosB-zApm*sinB, yZoomM*sinB+zApm*cosB);
		glVertex3d(xAmm, yZoomM*cosB-zAmm*sinB, yZoomM*sinB+zAmm*cosB);
		glVertex3d(xAmp, yZoomM*cosB-zAmp*sinB, yZoomM*sinB+zAmp*cosB);
		//front
		glVertex3d(xApp, yZoomP*cosB-zApp*sinB, yZoomP*sinB+zApp*cosB);
		glVertex3d(xApp, yZoomM*cosB-zApp*sinB, yZoomM*sinB+zApp*cosB);
		glVertex3d(xAmp, yZoomM*cosB-zAmp*sinB, yZoomM*sinB+zAmp*cosB);
		glVertex3d(xAmp, yZoomP*cosB-zAmp*sinB, yZoomP*sinB+zAmp*cosB);
		//top
		glVertex3d(xApp, yZoomP*cosB-zApp*sinB, yZoomP*sinB+zApp*cosB);
		glVertex3d(xApm, yZoomP*cosB-zApm*sinB, yZoomP*sinB+zApm*cosB);
		glVertex3d(xAmm, yZoomP*cosB-zAmm*sinB, yZoomP*sinB+zAmm*cosB);
		glVertex3d(xAmp, yZoomP*cosB-zAmp*sinB, yZoomP*sinB+zAmp*cosB);
		//left
		glVertex3d(xAmp, yZoomP*cosB-zAmp*sinB, yZoomP*sinB+zAmp*cosB);
		glVertex3d(xAmm, yZoomP*cosB-zAmm*sinB, yZoomP*sinB+zAmm*cosB);
		glVertex3d(xAmm, yZoomM*cosB-zAmm*sinB, yZoomM*sinB+zAmm*cosB);
		glVertex3d(xAmp, yZoomM*cosB-zAmp*sinB, yZoomM*sinB+zAmp*cosB);
		//right
		glVertex3d(xApp, yZoomP*cosB-zApp*sinB, yZoomP*sinB+zApp*cosB);
		glVertex3d(xApm, yZoomP*cosB-zApm*sinB, yZoomP*sinB+zApm*cosB);
		glVertex3d(xApm, yZoomM*cosB-zApm*sinB, yZoomP*sinB+zApm*cosB);
		glVertex3d(xApp, yZoomM*cosB-zApp*sinB, yZoomP*sinB+zApp*cosB);
		
		//This doesn't look as pretty with the lighting :(
/*		
		GL11.glBegin(GL11.GL_QUADS);
	    //back
	    GL11.glVertex2d(xApm, yZoomP*cosB-zApm*sinB);
	    GL11.glVertex2d(xApm, yZoomM*cosB-zApm*sinB);
	    GL11.glVertex2d(xAmm, yZoomM*cosB-zAmm*sinB);
	    GL11.glVertex2d(xAmm, yZoomP*cosB-zAmm*sinB);
	    //bottom
	    GL11.glVertex2d(xApp, yZoomM*cosB-zApp*sinB);
	    GL11.glVertex2d(xApm, yZoomM*cosB-zApm*sinB);
	    GL11.glVertex2d(xAmm, yZoomM*cosB-zAmm*sinB);
	    GL11.glVertex2d(xAmp, yZoomM*cosB-zAmp*sinB);
  		//front
	    GL11.glVertex2d(xApp, yZoomP*cosB-zApp*sinB);
	    GL11.glVertex2d(xApp, yZoomM*cosB-zApp*sinB);
	    GL11.glVertex2d(xAmp, yZoomM*cosB-zAmp*sinB);
	    GL11.glVertex2d(xAmp, yZoomP*cosB-zAmp*sinB);
  		//top
	    GL11.glVertex2d(xApp, yZoomP*cosB-zApp*sinB);
	    GL11.glVertex2d(xApm, yZoomP*cosB-zApm*sinB);
	    GL11.glVertex2d(xAmm, yZoomP*cosB-zAmm*sinB);
	    GL11.glVertex2d(xAmp, yZoomP*cosB-zAmp*sinB);
  		//left
	    GL11.glVertex2d(xAmp, yZoomP*cosB-zAmp*sinB);
	    GL11.glVertex2d(xAmm, yZoomP*cosB-zAmm*sinB);
	    GL11.glVertex2d(xAmm, yZoomM*cosB-zAmm*sinB);
	    GL11.glVertex2d(xAmp, yZoomM*cosB-zAmp*sinB);
		//right
	    GL11.glVertex2d(xApp, yZoomP*cosB-zApp*sinB);
	    GL11.glVertex2d(xApm, yZoomP*cosB-zApm*sinB);
	    GL11.glVertex2d(xApm, yZoomM*cosB-zApm*sinB);
		GL11.glVertex2d(xApp, yZoomM*cosB-zApp*sinB);
*/	
	}
	
	public static void setupLighting(){
		float lightAmbient[] = { 0.5f, 0.5f, 0.5f, 1.0f };	//sets ambient light
		float lightDiffuse[] = { 1f, 1f, 1f, 1.0f };		//sets light type to (1, 1, 1)
		float lightPosition[] = { 0.5f, 0.5f, 0.5f, 1f };			//sets light position to (0, 0, 0)
 
		GL11.glLightfv(GL11.GL_LIGHT1, GL11.GL_AMBIENT, asByteBuffer(lightAmbient));              // Setup The Ambient Light
		GL11.glLightfv(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, asByteBuffer(lightDiffuse));              // Setup The Diffuse Light         
		GL11.glLightfv(GL11.GL_LIGHT1, GL11.GL_POSITION,asByteBuffer(lightPosition));  
 
		GL11.glEnable(GL11.GL_LIGHT1); 
		GL11.glEnable ( GL11.GL_LIGHTING );
	}
	
	private static ByteBuffer asByteBuffer(float[] x){
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(x.length * Float.BYTES); //4 bytes per float
		byteBuf.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = byteBuf.asFloatBuffer();
		buffer.put(x);
		buffer.position(0);
		return byteBuf;
	}
	
	public static void increaseAngleA(double angle){
		angleA = angleA + angle;
	}
	
	public double getAngleA(){
		return angleA;
	}
	
	public static void increaseAngleB(double angle){
		angleB = angleB + angle;
	}
	
	public double getAngleB(){
		return angleB;
	}
	
	public static void increaseZoom(double increment){
		zoom = zoom*increment;
	}
	
	public double getZoom(){
		return zoom;
	}
	
	public static void setZoom(double z){
		zoom = z;
	}
	
	public static void setAngleA(double a){
		angleA = a;
	}
	
	public static void setAngleB(double b){
		angleB = b;
	}
	
	public static double getScale(){
		return SCALE;
	}
	
	public static void switchAxis(){
		axis ^= true;
	}

}

package yawp;

import processing.core.PApplet;

public class YawpMain extends PApplet {
	public void settings() {
		size(800,600);
	}
	
	public void setup() {
		
		surface.setResizable(true);
		
		javax.swing.JFrame jframe = (javax.swing.JFrame)((processing.awt.PSurfaceAWT.SmoothCanvas)getSurface().getNative()).getFrame();
		jframe.setLocation(0, 0);
		jframe.setExtendedState(jframe.getExtendedState() | jframe.MAXIMIZED_BOTH);
		delay(100);
		
		
		
		
	}
	
	public void draw() {
		System.out.println(width);
		System.out.println(height);
		background(0);
		stroke(255);
		line(0,0,width,height);
	}
	
	public static void main(String[] args) {
		PApplet.main(new String[] { yawp.YawpMain.class.getName() });
	}
}

package yawp;

import javax.swing.JFrame;

import processing.core.PApplet;
import processing.data.JSONObject;
import project.Project;

public class YawpMain extends PApplet {
	
	public static Project activeProject;
	
	public static JSONObject config;
	
	public void settings() {
		size(800,600);
	}
	
	public void setup() {
		// Maximize Window
		surface.setResizable(true);
		javax.swing.JFrame jframe = (javax.swing.JFrame)((processing.awt.PSurfaceAWT.SmoothCanvas)getSurface().getNative()).getFrame();
		jframe.setLocation(0, 0);
		jframe.setExtendedState(jframe.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		delay(100);
		
		PAppletBridge.p = this;
		
		config = this.loadJSONObject("config.json");
		
		// Setup GUI controls
		GUIControl.initialize(this);
	}
	
	public void draw() {
		background(100);
		stroke(255);
		line(0,0,width,height);
	}
	
	public static void main(String[] args) {
		PApplet.main(new String[] { yawp.YawpMain.class.getName() });
	}
}

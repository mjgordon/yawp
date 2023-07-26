package yawp;

import javax.swing.JFrame;

import processing.core.PApplet;
import processing.core.PFont;
import processing.event.MouseEvent;
import project.Project;

public class YawpMain extends PApplet {

	public static PFont font;

	private boolean startupFlag = true;
	
	private static Editor activeEditor;
	
	


	public void settings() {
		size(800, 600);
	}


	public void setup() {
		// Maximize Window
		surface.setResizable(true);
		
		// For default renderer
		
		javax.swing.JFrame jframe = (javax.swing.JFrame) ((processing.awt.PSurfaceAWT.SmoothCanvas) getSurface().getNative()).getFrame();
		jframe.setLocation(0, 0);
		jframe.setExtendedState(jframe.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		// For P2d renderer
		/*
		com.jogamp.newt.opengl.GLWindow window = (com.jogamp.newt.opengl.GLWindow)(surface.getNative());
	    window.setResizable(true);
	    window.setMaximized(true, true);
	    */
		delay(100);

		// Set the global PApplet reference
		PAppletBridge.p = this;

		// Load user config from JSON
		Config.loadConfig();

		font = this.loadFont("LiberationSans-12.vlw");
		
		activeEditor = new Editor();

	}


	public void draw() {
		/*
		 * Initialize the controlP5 system on the first frame. This is because even
		 * though the width is set in setup, this is not reflected in the internal
		 * PGraphics until the first draw loop, and controlP5 reads from this during
		 * setup. (Otherwise, mouse clicks outside the initial window size won't work)
		 */

		if (startupFlag) {
			GUIControl.initialize(this);
			startupFlag = false;
		}

		background(100);
		
		activeEditor.render();
	}
	
	public static void setProject(Project project) {
		activeEditor.setProject(project);
	}
	
	public static Project getProject() {
		return activeEditor.getProject();
	}
	
	public void mouseDragged() {
		activeEditor.mouseDragged();
	}
	
	
	public void mouseWheel(MouseEvent event) {
		 activeEditor.mouseWheel(event.getCount());
	}


	public static void main(String[] args) {
		PApplet.main(new String[] { yawp.YawpMain.class.getName() });
	}
}

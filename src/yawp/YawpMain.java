package yawp;

import javax.swing.JFrame;

import processing.core.PApplet;
import processing.core.PFont;
import project.Project;

public class YawpMain extends PApplet {

	public static Project activeProject;

	public static PFont font;

	private boolean startupFlag = true;


	public void settings() {
		size(800, 600);
	}


	public void setup() {
		// Maximize Window
		surface.setResizable(true);
		javax.swing.JFrame jframe = (javax.swing.JFrame) ((processing.awt.PSurfaceAWT.SmoothCanvas) getSurface().getNative()).getFrame();
		jframe.setLocation(0, 0);
		jframe.setExtendedState(jframe.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		delay(100);

		PAppletBridge.p = this;

		Config.loadConfig();

		font = this.loadFont("LiberationSans-12.vlw");

	}


	public void draw() {
		/*
		 * Initialize the controlP5 system on the first frame. This is because even
		 * though the width is set in setup, this is not reflected in the internal
		 * PGraphics until the first draw loop, and controlP5 reads from this during
		 * setup. (Otherwise, mouse clicks outside the initial window size won't work)
		 * 
		 * 
		 */

		if (startupFlag) {
			GUIControl.initialize(this);
			startupFlag = false;
		}

		background(100);
		stroke(255);
		line(0, 0, width, height);
	}


	public static void main(String[] args) {
		PApplet.main(new String[] { yawp.YawpMain.class.getName() });
	}
}

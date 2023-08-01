package yawp;

import drop.*;
import javax.swing.JFrame;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.MouseEvent;

import project.Project;

public class YawpMain extends PApplet {

	public static PFont font;

	private boolean startupFlag = true;

	private static Editor activeEditor;
	
	private SDrop drop;


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
		 * com.jogamp.newt.opengl.GLWindow window =
		 * (com.jogamp.newt.opengl.GLWindow)(surface.getNative());
		 * window.setResizable(true); window.setMaximized(true, true);
		 */
		delay(100);

		// Set the global PApplet reference
		PAppletBridge.p = this;

		// Load user config from JSON
		Config.loadConfig();

		font = this.loadFont("LiberationSans-12.vlw");

		activeEditor = new Editor();

		drop = new SDrop(this);
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
	
	
	public void dropEvent(DropEvent e) {
		/*
		  // returns a string e.g. if you drag text from a texteditor
		  // into the sketch this can be handy.
		  println("toString()\t"+theDropEvent.toString());

		  // returns true if the dropped object is an image from
		  // the harddisk or the browser.
		  println("isImage()\t"+theDropEvent.isImage());

		  // returns true if the dropped object is a file or folder.
		  println("isFile()\t"+theDropEvent.isFile());

		  // if the dropped object is a file or a folder you 
		  // can access it with file() . for more information see
		  // http://java.sun.com/j2se/1.4.2/docs/api/java/io/File.html
		  println("file()\t"+theDropEvent.file());

		  // returns true if the dropped object is a bookmark, a link, or a url.  
		  println("isURL()\t"+theDropEvent.isURL());

		  // returns the url as string.
		  println("url()\t"+theDropEvent.url());

		  // returns the DropTargetDropEvent, for further information see
		  // http://java.sun.com/j2se/1.4.2/docs/api/java/awt/dnd/DropTargetDropEvent.html
		  println("dropTargetDropEvent()\t"+theDropEvent.dropTargetDropEvent());
		  */
		
		if (e.isFile()) {
			if (e.isImage()) {
				Project project = activeEditor.getProject();
				
				if (project != null) {
					new GUIControl().selectImageCallback(e.file());
					//project.loadImage(e.file());	
				}
				
			}
		}
	}


	public static void main(String[] args) {
		PApplet.main(new String[] { yawp.YawpMain.class.getName() });
	}
}

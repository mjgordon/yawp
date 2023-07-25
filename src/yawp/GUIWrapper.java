package yawp;

import controlP5.*;

/**
 * Flow layouts for controlP5 objects Handles positioning within a tab but not
 * resizing
 *
 */
public class GUIWrapper {

	public Group group;

	private int gutter = 10;
	private int currentX = gutter;
	private int currentY = gutter;

	private int lineHeight = 0;

	private boolean debug = false;


	public GUIWrapper(Group group) {
		this.group = group;
		if (debug) {
			System.out.println("Create Wrapper");
		}
	}


	public GUIWrapper flow(ControllerInterface<?> theElement) {
		if (currentX + theElement.getWidth() + gutter > group.getWidth()) {
			newLine();
		}

		if (theElement.getHeight() > lineHeight) {
			lineHeight = theElement.getHeight();
		}
		theElement.setPosition(currentX, currentY);
		
		if (debug) {
			System.out.println(theElement + " | Add at : " + currentX + "," + currentY);
		}

		currentX += theElement.getWidth() + gutter;

		return this;
	}


	public GUIWrapper newLine() {
		currentX = gutter;
		currentY += lineHeight + gutter;
		lineHeight = 0;
		if (debug) {
			System.out.println("newline");
		} 
		return this;
	}


	public GUIWrapper newLine(int y) {
		currentX = gutter;
		currentY += y;
		lineHeight = 0;
		if (debug) {
			System.out.println("newline " + y);
		} 
		return this;
	}
}

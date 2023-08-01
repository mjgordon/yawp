package yawp;

import java.util.ArrayList;

import controlP5.*;

/**
 * Flow layouts for controlP5 objects Handles positioning within a tab but not
 * resizing
 *
 */
public class GUIWrapper {

	public Group group;
	
	public ArrayList<ControllerInterface<?>> children;

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
		
		children = new ArrayList<ControllerInterface<?>>();
	}


	public GUIWrapper flow(ControllerInterface<?> theElement) {
		
		boolean isGroup = theElement instanceof Group;
		int eHeight;
		if (isGroup) {
			Group eGroup = (Group) theElement;
			
			eHeight = eGroup.getBackgroundHeight() + eGroup.getBarHeight();
		}
		else {
			eHeight = theElement.getHeight();
		}
		
		if (currentX + theElement.getWidth() + gutter > group.getWidth()) {
			newLine();
		}

		if (eHeight > lineHeight) {
			lineHeight = eHeight;
		}
		
		if (isGroup) {
			Group eGroup = (Group) theElement;
			theElement.setPosition(currentX, currentY + eGroup.getBarHeight());
		}
		else {
			theElement.setPosition(currentX, currentY);
		}
		
		if (debug) {
			System.out.println(theElement + " | Add at : " + currentX + "," + currentY);
		}

		currentX += theElement.getWidth() + gutter;
		
		children.add(theElement);

		return this;
	}
	
	
	public void clear() {
		for (ControllerInterface<?> ci : children) {
			group.remove(ci);
			ci.remove();
		}
		children.clear();
		
		currentX = gutter;
		currentY = gutter;
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

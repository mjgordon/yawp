package yawp;

import processing.core.PMatrix3D;
import processing.core.PVector;
import project.Page;
import project.Project;

import static yawp.PAppletBridge.p;

public class Editor {
	
	private Project project = null;
	private Page openPage;
	
	private PVector viewOffset;
	
	public Editor() {
		viewOffset = new PVector();
	}
	
	public void render() {
		if (project == null) {
			return;
		}
		
		p.pushMatrix();
		p.translate(viewOffset.x, viewOffset.y);

		
		
		openPage.render();
		
		p.popMatrix();
	}
	
	public void setProject(Project project) {
		this.project = project;
		this.openPage = project.getPage(0);
	}
	
	public Project getProject() {
		return project;
	}
	
	public void mouseDragged() {
		int dx = p.mouseX - p.pmouseX;
		int dy = p.mouseY - p.pmouseY;
		
		if (p.mouseButton == p.RIGHT) {
			viewOffset.x += dx;
			viewOffset.y += dy;
			System.out.println(dx + " : " + dy);
		}
	}
}

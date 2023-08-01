package yawp;

import processing.core.PConstants;
import processing.core.PVector;
import project.Page;
import project.Project;

import static yawp.PAppletBridge.p;

public class Editor {

	private Project project = null;
	private Page openPage;

	private PVector viewOffset;

	private float scale = 1;
	
	private EditorMode mode = EditorMode.SELECT;


	public Editor() {
		viewOffset = new PVector();
	}


	public void render() {
		if (project == null) {
			return;
		}

		p.pushMatrix();
		p.translate(viewOffset.x, viewOffset.y);
		p.scale(scale);

		openPage.render();

		p.popMatrix();
	}


	public void setProject(Project project) {
		this.project = project;
		this.openPage = project.getPage(0);
		viewOffset = new PVector((p.width/ 2) - (openPage.getWidth() / 2),(p.height / 2) - (openPage.getHeight() / 2));
	}


	public Project getProject() {
		return project;
	}


	public void mouseDragged() {
		int dx = p.mouseX - p.pmouseX;
		int dy = p.mouseY - p.pmouseY;

		if (p.mouseButton == PConstants.RIGHT) {
			viewOffset.x += dx;
			viewOffset.y += dy;
		}
	}


	public void mouseWheel(int delta) {
		PVector mousePre = screenToDocument(p.mouseX,p.mouseY);

		if (delta < 0) {
			scale *= 1.1;
		}

		if (delta > 0) {
			scale *= 0.9;
		}

		if (scale < 0.1) {
			scale = 0.1f;
		}
		
		PVector mousePost = screenToDocument(p.mouseX,p.mouseY);
		viewOffset.add(PVector.sub(mousePost, mousePre).mult(scale));
	}
	
	private PVector screenToDocument(float x, float y) {
		PVector output = new PVector(x,y).sub(viewOffset).div(scale);
		return output;
	}
}

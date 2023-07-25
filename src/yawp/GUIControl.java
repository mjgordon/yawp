package yawp;

import controlP5.*;
import java.io.File;
import processing.core.PApplet;
import processing.data.JSONObject;
import project.Project;

import static yawp.PAppletBridge.p;


/**
 * Setup for interface elements such as buttons, sliders, etc. Using
 * controlP5
 *
 */
public class GUIControl {
	private static ControlP5 cp5;
	private static Tab tabProject;
	private static Tab tabWorkspace;
	private static Tab tabNewProject;
	

	public static void initialize(PApplet pApplet) {
		cp5 = new ControlP5(pApplet);
		
		setupProjectTab();
		setupWorkspaceTab();
		setupNewProjectTab();
		
		cp5.getTab("default").setVisible(false);

		tabProject.bringToFront();
	}
	
	
	public static void setupProjectTab() {
		tabProject = cp5.addTab("Project");
		
		cp5.addButton("buttonNewProject").setLabel("New Project")
		.setPosition(100, 100)
		.setSize(200, 50)
		.moveTo(tabProject)
		.onPress(new CallbackListener() {
			public void controlEvent(CallbackEvent e) {
				tabNewProject.setVisible(true);
				tabNewProject.bringToFront();
			}
		});
		
		cp5.addButton("buttonLoadProject").setLabel("Load Project")
		.setPosition(310, 100)
		.setSize(200,50)
		.moveTo(tabProject)
		.onPress(new CallbackListener() {
			public void controlEvent(CallbackEvent e) {
				p.selectFolder("Open Project Directory","selectProjectDirectoryCallback", null, new GUIControl());
			}
		});
	}
	
	
	public static void setupWorkspaceTab() {
		tabWorkspace = cp5.addTab("Workspace");
		
		cp5.addButton("buttonExport")
		.setLabel("Export to PDF")
		.setPosition(100,100)
		.setSize(100,50)
		.moveTo(tabWorkspace)
		.onClick(new CallbackListener() {
			public void controlEvent(CallbackEvent e) {
				YawpMain.activeProject.exportToPDF();
			}
		});
		
		tabWorkspace.setVisible(false);
	}


	public static void setupNewProjectTab() {
		tabNewProject = cp5.addTab("New Project");
		
		cp5.addButton("buttonSelectProjectDirectory")
		.setLabel("Select Directory")
		.setPosition(100, 100)
		.setSize(100, 50)
		.moveTo(tabNewProject)
		.onClick(new CallbackListener() {
			public void controlEvent(CallbackEvent e) {
				p.selectFolder("Select New Project Directory","selectNewProjectDirectoryCallback", null, new GUIControl());
			}
		});
		
		Textlabel labelCurrentProjectDirectory = cp5.addTextlabel("labelCurrentProjectDirectory")
		.setText("")
		.setPosition(205,100)
		.setColorValue(0xFFFFFFFF)
		.moveTo(tabNewProject);
		
		Textfield textfieldProjectName = cp5.addTextfield("textfieldProjectName")
		.setSize(200,50)
		.setPosition(100,160)
		.setLabel("Project Name")
		.moveTo(tabNewProject)
		.setAutoClear(false);
		
		DropdownList dropdownPageSize = cp5.addDropdownList("dropdownPageSize")
				.setSize(200,100)
				.setPosition(500,100)
				.setLabel("Default Page Size")
				.addItem("A3","A3")
				.addItem("A4","A4")
				.moveTo(tabNewProject)
				.close();
		
		cp5.addButton("buttonNewProjectFinalize")
		.setSize(100,100)
		.setPosition(100,270)
		.setLabel("Create Project")
		.moveTo(tabNewProject)
		.onPress(new CallbackListener() {
			public void controlEvent(CallbackEvent e) {
				String name = textfieldProjectName.getText();
				String directory = labelCurrentProjectDirectory.getStringValue();
				String pageSize = dropdownPageSize.getLabel();
				if (pageSize.equals("Default Page Size")) {
					pageSize = "A4";
				}
				
				if (name.equals("") || directory.equals("")) {
					return;
				}
				YawpMain.activeProject = new Project(name,directory);
				YawpMain.activeProject.addPage(pageSize);
				YawpMain.activeProject.saveToDisk();
				
				textfieldProjectName.setText("");
				labelCurrentProjectDirectory.setText("");
				
				tabWorkspace.setVisible(true);
				tabWorkspace.bringToFront();
				tabNewProject.setVisible(false);		
			}
		});
			
		tabNewProject.setVisible(false);
	}
	
	
	public void selectProjectDirectoryCallback(File selection) {
		if (selection == null) {
			return;
		}
		
		JSONObject projectJSON = p.loadJSONObject(selection.getAbsolutePath() + "/project.json");
		
		if (projectJSON == null) {
			return;
		}
		
		YawpMain.activeProject = new Project(projectJSON, selection.getAbsolutePath());
		
		tabWorkspace.setVisible(true);
		tabWorkspace.bringToFront();
	}
	
	
	public void selectNewProjectDirectoryCallback(File selection) {
		if (selection == null) {
			return;
		}
		cp5.get("labelCurrentProjectDirectory").setStringValue(selection.getAbsolutePath());
	}
}

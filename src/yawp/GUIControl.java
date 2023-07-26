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
	
	public static int colorGUIBG;
	public static int colorGUIMain;
	

	public static void initialize(PApplet pApplet) {
		cp5 = new ControlP5(pApplet,YawpMain.font);
		
		setupColors();
		cp5.setColorBackground(colorGUIMain);
		cp5.setBackground(colorGUIMain);
		
		
		
		setupProjectTab();
		setupWorkspaceTab();
		setupNewProjectTab();
		
		cp5.getTab("default").setVisible(false);

		tabProject.bringToFront();
	}
	
	
	private static void setupProjectTab() {
		tabProject = cp5.addTab("Project")
				.setColorBackground(colorGUIBG)
				.setColorActive(colorGUIMain);
		
		Group group = cp5.addGroup("groupProject")
				.setLabel("")
				.setPosition(100,100)
				.setWidth(120)
				.setBackgroundHeight(200)
				.setBackgroundColor(colorGUIBG)
				
				.disableCollapse()
				.moveTo(tabProject);
		
		Button buttonNewProject = cp5.addButton("buttonNewProject").setLabel("New Project")
		.setSize(100, 20)
		.moveTo(tabProject)
		.setGroup(group)
		.onClick(new CallbackListener() {
			public void controlEvent(CallbackEvent e) {
				tabNewProject.setVisible(true);
				tabNewProject.bringToFront();
			}
		});
		
		Button buttonLoadProject = cp5.addButton("buttonLoadProject").setLabel("Load Project")
		.setSize(100,20)
		.moveTo(tabProject)
		.setGroup(group)
		.onClick(new CallbackListener() {
			public void controlEvent(CallbackEvent e) {
				p.selectFolder("Open Project Directory","selectProjectDirectoryCallback", null, new GUIControl());
			}
		});
		
		GUIWrapper wrapper = new GUIWrapper(group);
		wrapper.flow(buttonNewProject);
		wrapper.flow(buttonLoadProject);
	}
	
	
	private static void setupWorkspaceTab() {
		tabWorkspace = cp5.addTab("Workspace")
				.setColorBackground(colorGUIBG)
				.setColorActive(colorGUIMain);
		
		cp5.addButton("buttonExport")
		.setLabel("Export to PDF")
		.setPosition(100,100)
		.setSize(100,50)
		.moveTo(tabWorkspace)
		.onClick(new CallbackListener() {
			public void controlEvent(CallbackEvent e) {
				YawpMain.getProject().exportToPDF();
			}
		});
		
		tabWorkspace.setVisible(false);
	}


	private static void setupNewProjectTab() {
		tabNewProject = cp5.addTab("New Project")
				.setColorBackground(colorGUIBG)
				.setColorActive(colorGUIMain);
		
		Group group = cp5.addGroup("groupNewProject")
				.setLabel("")
				.setPosition(800,200)
				.setWidth(400)
				.setBackgroundHeight(400)
				.setBackgroundColor(colorGUIBG)
				.disableCollapse()
				.moveTo(tabNewProject);
		
		GUIWrapper wrapper = new GUIWrapper(group);
		
		Button buttonSelectProjectDirectory = cp5.addButton("buttonSelectProjectDirectory")
		.setLabel("Select Directory")
		.setSize(200, 20)
		.moveTo(tabNewProject)
		.setGroup(group)
		.onClick(new CallbackListener() {
			public void controlEvent(CallbackEvent e) {
				p.selectFolder("Select New Project Directory","selectNewProjectDirectoryCallback", null, new GUIControl());
			}
		});
		
		Textlabel labelCurrentProjectDirectory = cp5.addTextlabel("labelCurrentProjectDirectory")
		.setText("")
		.setSize(200,20)
		.setColorValue(0xFFFFFFFF)
		.moveTo(tabNewProject)
		.setGroup(group);
		
		Textfield textfieldProjectName = cp5.addTextfield("textfieldProjectName")
		.setSize(200,20)
		.setLabel("Project Name")
		.moveTo(tabNewProject)
		.setValueLabel("yo")
		.setGroup(group)
		.setAutoClear(false);
		
		DropdownList dropdownPageSize = cp5.addDropdownList("dropdownPageSize")
				.setWidth(200)
				.setBarHeight(20)
				.setLabel("Default Page Size")
				.addItem("A3","A3")
				.addItem("A4","A4")
				.moveTo(tabNewProject)
				.setGroup(group)
				.close();
		
		Button buttonNewProjectFinalize = cp5.addButton("buttonNewProjectFinalize")
		.setSize(200,20)
		.setLabel("Create Project")
		.moveTo(tabNewProject)
		.setGroup(group)
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
				Project project = new Project(name,directory);
				YawpMain.setProject(project);
				project.addPage(pageSize);
				project.saveToDisk();
				
				textfieldProjectName.setText("");
				labelCurrentProjectDirectory.setText("");
				
				tabWorkspace.setVisible(true);
				tabWorkspace.bringToFront();
				tabNewProject.setVisible(false);
			}
		});
		
		dropdownPageSize.bringToFront();
		
		wrapper.flow(buttonSelectProjectDirectory);
		wrapper.flow(labelCurrentProjectDirectory);
		wrapper.flow(textfieldProjectName);
		wrapper.newLine(50);
		wrapper.flow(dropdownPageSize);
		wrapper.flow(buttonNewProjectFinalize);
			
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
		
		YawpMain.setProject(new Project(projectJSON, selection.getAbsolutePath()));
		
		tabWorkspace.setVisible(true);
		tabWorkspace.bringToFront();
	}
	
	
	public void selectNewProjectDirectoryCallback(File selection) {
		if (selection == null) {
			return;
		}
		cp5.get("labelCurrentProjectDirectory").setStringValue(selection.getAbsolutePath());
	}
	
	private static void setupColors() {

		colorGUIBG = Config.getColor("gui.colors.bg");
		colorGUIMain = Config.getColor("gui.colors.main");
	}
	
}

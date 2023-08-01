package yawp;

import controlP5.*;
import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

import processing.core.PApplet;
import processing.data.JSONObject;
import project.PDImage;
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
	public static int colorGUIBGLight;
	
	public static Consumer<Project> regenerateImageDataList;
	
	public static int tempCounter = 0;
	

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
				.setPosition(10,30)
				.setWidth(170)
				.setBackgroundHeight(200)
				.setBackgroundColor(colorGUIBG)
				
				.disableCollapse()
				.moveTo(tabProject);
		
		Button buttonNewProject = cp5.addButton("buttonNewProject").setLabel("New Project")
		.setSize(150, 20)
		.moveTo(tabProject)
		.setGroup(group)
		.onClick(new CallbackListener() {
			public void controlEvent(CallbackEvent e) {
				tabNewProject.setVisible(true);
				tabNewProject.bringToFront();
			}
		});
		
		Button buttonLoadProject = cp5.addButton("buttonLoadProject").setLabel("Load Project")
		.setSize(150,20)
		.moveTo(tabProject)
		.setGroup(group)
		.onClick(new CallbackListener() {
			public void controlEvent(CallbackEvent e) {
				p.selectFolder("Open Project Directory","selectProjectDirectoryCallback", null, new GUIControl());
			}
		});
		
		Button buttonLoadLast = cp5.addButton("buttonLoadLast").setLabel("Load Last Project")
				.setSize(150,20)
				.moveTo(tabProject)
				.setGroup(group)
				.onClick(new CallbackListener() {
					public void controlEvent(CallbackEvent e) {
						JSONObject lastProject = Config.getUserObject("lastProject");
						loadProject(lastProject.getString("project.directory"));
					}
				});
		
		GUIWrapper wrapper = new GUIWrapper(group);
		wrapper.flow(buttonNewProject);
		wrapper.flow(buttonLoadProject);
		wrapper.flow(buttonLoadLast);
	}
	
	
	private static void setupWorkspaceTab() {
		tabWorkspace = cp5.addTab("Workspace")
				.setColorBackground(colorGUIBG)
				.setColorActive(colorGUIMain);
		
		Group groupLeft = cp5.addGroup("groupWorkspaceLeft")
				.setLabel("Document")
				.setPosition(10,30)
				.setSize(300, p.height - 40)
				.setBackgroundColor(colorGUIBG)
				.disableCollapse()
				.moveTo(tabWorkspace);
		
		Group groupRight = cp5.addGroup("groupWorkspaceRight")
				.setLabel("Editor")
				.setPosition(p.width - 300 - 10,30)
				.setSize(300, p.height - 40)
				.setBackgroundColor(colorGUIBG)
				.disableCollapse()
				.moveTo(tabWorkspace);
		
		Button buttonExport = cp5.addButton("buttonExport")
				.setLabel("Export to PDF")
				.setSize(100,50)
				.moveTo(tabWorkspace)
				.setGroup(groupLeft)
				.onClick(new CallbackListener() {
					public void controlEvent(CallbackEvent e) {
						YawpMain.getProject().saveToDisk();
						YawpMain.getProject().exportToPDF();
					}
				});
		
		Button buttonSaveProject = cp5.addButton("buttonSaveProject")
				.setLabel("Save Project")
				.setSize(100,50)
				.moveTo(tabWorkspace)
				.setGroup(groupLeft)
				.onClick(new CallbackListener() {
					public void controlEvent(CallbackEvent e) {
						YawpMain.getProject().saveToDisk();
					}
				});
		
		GUIWrapper guiWrapperLeft = new GUIWrapper(groupLeft);
		guiWrapperLeft.flow(buttonSaveProject);
		guiWrapperLeft.flow(buttonExport);
		
		Button buttonAddImage = cp5.addButton("buttonAddImage")
				.setLabel("Add Image")
				.setPosition(100,100)
				.setSize(100,50)
				.moveTo(tabWorkspace)
				.setGroup(groupRight)
				.onClick(new CallbackListener() {
					public void controlEvent(CallbackEvent e) {
						p.selectInput("Select Image","selectImageCallback", null, new GUIControl());
					}
				});
		
		Group groupImageData = cp5.addGroup("groupImageData")
				.setLabel("Project Images")
				.setSize(groupRight.getWidth() - 20, 200)
				.setBackgroundColor(colorGUIBGLight)
				.moveTo(tabWorkspace)
				.setGroup(groupRight)
				.setBackgroundHeight(100);
		
		GUIScroll guiScrollImageData = new GUIScroll(groupImageData,120,4);
		
		
		regenerateImageDataList = project -> {
			guiScrollImageData.clear();
			ArrayList<PDImage> imageData = project.getImageDataList();
			
			for (PDImage pdi : imageData) {
				Button button = cp5.addButton("buttonImage_" + tempCounter())
						.setLabel("")
						.setSize(120,120)
						.moveTo(tabWorkspace)
						.setGroup(groupImageData)
						.onClick(new CallbackListener() {
							public void controlEvent(CallbackEvent e) {
								System.out.println("button!");
							}
						})
						.setImages(pdi.getIcons());
				
				guiScrollImageData.add(button);
			}
			guiScrollImageData.scroll(0);
		};
		
		GUIWrapper guiWrapperRight = new GUIWrapper(groupRight);
		guiWrapperRight.flow(buttonAddImage);
		guiWrapperRight.flow(groupImageData);
		
		
			
		
	
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
	
	
	public static Project loadProject(String path) {
		JSONObject projectJSON = p.loadJSONObject(path + "/project.json");
		
		if (projectJSON == null) {
			return null;
		}
		
		Project project = new Project(projectJSON, path);
		
		YawpMain.setProject(project);
		
		tabWorkspace.setVisible(true);
		tabWorkspace.bringToFront();
		
		return project;
	}
	
	
	public void selectProjectDirectoryCallback(File selection) {
		if (selection == null) {
			return;
		}
		
		Project project = loadProject(selection.getAbsolutePath());
		
		JSONObject lastOpened = new JSONObject();
		lastOpened.setString("project.name", project.getName());
		lastOpened.setString("project.directory",project.getDirectory());
		
		Config.setUserObject("lastProject", lastOpened);
		Config.saveUserConfig();
	}
	
	
	public void selectNewProjectDirectoryCallback(File selection) {
		if (selection == null) {
			return;
		}
		cp5.get("labelCurrentProjectDirectory").setStringValue(selection.getAbsolutePath());
	}
	
	
	public void selectImageCallback(File selection) {
		cp5.blockDraw = true;
		YawpMain.getProject().loadImage(selection);
		regenerateImageDataList.accept(yawp.YawpMain.getProject());
		cp5.blockDraw = false;
	}
	
	
	private static void setupColors() {
		colorGUIBG = Config.getColor("gui.colors.bg");
		colorGUIMain = Config.getColor("gui.colors.main");
		colorGUIBGLight = Config.getColor("gui.colors.bgLight");
	}
	
	private static int tempCounter() {
		return(++tempCounter);
	}
	
}

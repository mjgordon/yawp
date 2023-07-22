package project;

import static yawp.PAppletBridge.p;

import java.util.ArrayList;

import processing.data.JSONArray;
import processing.data.JSONObject;

public class Project {
	
	private String name;
	private String directory;
	
	private ArrayList<Page> pages;
	
	public Project(String name, String directory) {
		this.name = name;
		this.directory = directory;
		
		pages = new ArrayList<Page>();
	}
	
	public Project(JSONObject input, String filepath) {
		this.name = input.getString("projectName");
		this.directory = filepath;
		
		pages = new ArrayList<Page>();
		input.getJSONArray(filepath);
		
		JSONArray pagesArray = input.getJSONArray("pages");
		for (int i = 0; i < pagesArray.size(); i++) {
			pages.add(new Page(pagesArray.getJSONObject(i)));
		}
	}
	
	public void saveToDisk() {
		JSONObject output = new JSONObject();
		output.setString("projectName",name);
		
		JSONArray pagesJSON = new JSONArray();
		for (Page page : pages) {
			pagesJSON.append(page.getJSON());
		}
		
		output.put("pages", pagesJSON);
		
		
		p.saveJSONObject(output, directory + "/" + name + "/project.json");
	}
	
	public void addPage(String size) {
		if (Page.pageSizes.containsKey(size)) {
			pages.add(new Page(Page.pageSizes.get(size)));
		}
	}

}

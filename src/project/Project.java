package project;

import static yawp.PAppletBridge.p;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import processing.data.JSONArray;
import processing.data.JSONObject;
import yawp.Config;

public class Project {

	private String name;
	private String projectRootDirectory;
	
	private HashMap<UUID,PDImage> images;

	private ArrayList<Page> pages;


	public Project(String name, String directory) {
		this.name = name;
		this.projectRootDirectory = directory;

		pages = new ArrayList<Page>();
		images = new HashMap<UUID,PDImage>();
	}


	public Project(JSONObject input, String filepath) {
		this.name = input.getString("projectName");
		this.projectRootDirectory = filepath;

		pages = new ArrayList<Page>();
		images = new HashMap<UUID,PDImage>();
		
		input.getJSONArray(filepath);

		JSONArray pagesArray = input.getJSONArray("pages");
		for (int i = 0; i < pagesArray.size(); i++) {
			pages.add(new Page(pagesArray.getJSONObject(i)));
		}
	}


	public void addPage(String size) {
		if (Page.pageSizes.containsKey(size)) {
			pages.add(new Page(Page.pageSizes.get(size)));
		}
	}
	
	public Page getPage(int n) {
		return pages.get(n);
	}


	public void saveToDisk() {
		JSONObject output = new JSONObject();
		output.setString("projectName", name);

		JSONArray pagesJSON = new JSONArray();
		for (Page page : pages) {
			pagesJSON.append(page.getJSON());
		}
		output.put("pages", pagesJSON);
		
		JSONArray pdImageJSON = new JSONArray();
		for (UUID pdiUUID : images.keySet()) {
			pdImageJSON.append(images.get(pdiUUID).getJSONObject());
		}
		output.put("imageData", pdImageJSON);

		p.saveJSONObject(output, projectRootDirectory + "/project.json");
		
		savePDImagePreviews();
	}


	public void exportToPDF() {
		String pdfLatexPath = Config.getUserString("export.pdflatex.path");
		String latexPath = projectRootDirectory + "/output_latex/" + name + ".tex";
		String pdfPath = "output_pdf/"; // Must be local path

		String[] latex = getLatexRepresentation();
		p.saveStrings(latexPath, latex);

		try {
			ProcessBuilder pb = new ProcessBuilder(pdfLatexPath, "-output-directory=" + pdfPath, latexPath);
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);
			pb.directory(new File(projectRootDirectory));
			System.out.println(pb.command());
			Process process = pb.start();
			

			int exitValue = process.waitFor();
			System.out.println("pdflatex export exit value : " + exitValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void loadImage(File path) {
		PDImage pdImage = new PDImage(this,path);	
		
		boolean flag = true;
		for (UUID uuid : images.keySet()) {
			PDImage pd = images.get(uuid);
			if (pd.getRelativePath().equals(pdImage.getRelativePath())) {
				flag = false;
			}
		}
			
		if (flag) {
			images.put(pdImage.uuid, pdImage);	
		}
	}


	private String[] getLatexRepresentation() {
		ArrayList<String> out = new ArrayList<String>();

		out.add("\\documentclass{article}");
		out.add("\\begin{document}");
		out.add("\\LaTeX{}");

		out.add("\\end{document}");

		return out.toArray(new String[out.size()]);
	}
	
	
	public void savePDImagePreviews() {
		for (UUID uuid : images.keySet()) {
			PDImage pdi = images.get(uuid);
			pdi.savePreview();
		}
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public String getDirectory() {
		return projectRootDirectory;
	}
	
	
	public ArrayList<PDImage> getImageDataList() {
		return new ArrayList<PDImage>(images.values());
	}
}

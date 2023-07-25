package project;

import static yawp.PAppletBridge.p;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import processing.data.JSONArray;
import processing.data.JSONObject;
import yawp.Config;

public class Project {

	private String name;
	private String projectRootDirectory;

	private ArrayList<Page> pages;


	public Project(String name, String directory) {
		this.name = name;
		this.projectRootDirectory = directory;

		pages = new ArrayList<Page>();
	}


	public Project(JSONObject input, String filepath) {
		this.name = input.getString("projectName");
		this.projectRootDirectory = filepath;

		pages = new ArrayList<Page>();
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


	public void saveToDisk() {
		JSONObject output = new JSONObject();
		output.setString("projectName", name);

		JSONArray pagesJSON = new JSONArray();
		for (Page page : pages) {
			pagesJSON.append(page.getJSON());
		}

		output.put("pages", pagesJSON);

		p.saveJSONObject(output, projectRootDirectory + "/" + name + "/project.json");
	}


	public void exportToPDF() {
		String pdfLatexPath = Config.getString("export.pdflatex.path");
		String latexPath = projectRootDirectory + "output_latex/" + name + ".tex";
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


	private String[] getLatexRepresentation() {
		ArrayList<String> out = new ArrayList<String>();

		out.add("\\documentclass{article}");
		out.add("\\begin{document}");
		out.add("\\LaTeX{}");

		out.add("\\end{document}");

		return out.toArray(new String[out.size()]);
	}

}

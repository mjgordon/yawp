package project;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.data.JSONObject;

import static yawp.PAppletBridge.p;

public class PDImage extends ProjectData {
	private static final int maxPreviewSize = 1000;
	private static final int maxIconSize = 100;

	private PImage preview;
	private PImage iconDefault;
	private PImage iconHover;
	private PImage iconActive;

	private String relativePath;
	private String previewPath;


	public PDImage(Project parent, File path) {
		System.out.println("New PDImage at : " + path);
		uuid = UUID.randomUUID();

		Path parentPath = Paths.get(parent.getDirectory());
		Path thisPath = Paths.get(path.getAbsolutePath());

		previewPath = parent.getDirectory() + "/imagePreviews/";

		relativePath = parentPath.relativize(thisPath).toString();

		PImage image = p.loadImage(relativePath);

		preview = getScaledImage(image, maxPreviewSize);
		
		PGraphics icon = p.createGraphics(maxIconSize,maxIconSize);
		PImage iconScaled = getScaledImage(image, maxIconSize);
		
		icon.beginDraw();
		icon.background(50);
		icon.imageMode(p.CENTER);
		icon.image(iconScaled,icon.width / 2, icon.height / 2);
		icon.endDraw();
		iconDefault = icon.get();
		
		icon.beginDraw();
		icon.stroke(255);
		icon.noFill();
		icon.rect(0,0,icon.width - 1,icon.height - 1);
		icon.endDraw();
		iconHover = icon.get();
		
		icon.beginDraw();
		icon.stroke(0);
		icon.rect(0,0,icon.width - 1,icon.height - 1);
		icon.endDraw();
		iconActive = icon.get();
	}


	private PImage getScaledImage(PImage image, int maxSize) {
		int previewSize = Math.min(maxSize, Math.max(image.width, image.height));

		PImage out = image.copy();

		if (previewSize >= maxSize) {
			if (image.width > image.height) {
				out.resize(previewSize, previewSize / image.width * image.height);
			}
			else {
				out.resize(previewSize / image.height * image.width, previewSize);
			}
		}

		return out;
	}


	public JSONObject getJSONObject() {
		JSONObject output = new JSONObject();
		output.put("uuid", uuid.toString());
		output.put("relativePath", relativePath);
		output.put("previewPath", previewPath);

		return output;
	}


	public String getRelativePath() {
		return relativePath;
	}


	public void savePreview() {
		preview.save(previewPath + uuid + ".jpg");
	}
	
	
	public PImage[] getIcons() {
		return new PImage[] {iconDefault, iconHover, iconActive};
	}
}

package project;

import java.util.Map;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.JSONObject;

import static java.util.Map.entry;


public class Page {
	private PVector dimensions;
	
	public static Map<String, PVector> pageSizes = Map.ofEntries(
			entry("A3",new PVector(297,420)),
			entry("A4",new PVector(210,297)));
					
			
	
	public Page(PVector dimensions) {
		this.dimensions = dimensions;
	}
	
	
	
	public void render(PGraphics g) {
		g.fill(255);
		
		g.rect(0,0,dimensions.x,dimensions.y);
		
		
	}
	
	public JSONObject getJSON() {
		JSONObject output = new JSONObject();
		
		output.setFloat("dimensionsX", dimensions.x);
		output.setFloat("dimensionsY", dimensions.y);
		
		return output;
	}
}
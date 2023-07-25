package yawp;

import processing.data.JSONObject;

import static yawp.PAppletBridge.p;

public class Config {
	private static JSONObject configJSON;
	
	public static void loadConfig() {
		configJSON = p.loadJSONObject("config.json");
	}
	
	public static String getString(String input) {
		String out = configJSON.getString(input);
		if (out == null) {
			System.out.println("Key '" + input + "' not found in config");
		}
		return out;
	}
}
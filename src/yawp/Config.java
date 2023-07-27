package yawp;

import processing.data.JSONObject;

import static yawp.PAppletBridge.p;

public class Config {
	private static JSONObject configJSON;
	private static JSONObject userConfigJSON;
	
	public static void loadConfig() {
		configJSON = p.loadJSONObject("config.json");
		
		userConfigJSON = p.loadJSONObject("config_user.json");
		if (userConfigJSON == null) {
			userConfigJSON = new JSONObject();
		}
		
	}
	
	
	public static String getString(String input) {
		String out = configJSON.getString(input);
		if (out == null) {
			System.out.println("Key '" + input + "' not found in config");
		}
		return out;
	}
	
	public static String getUserString(String input) {
		String out = userConfigJSON.getString(input);
		if (out == null) {
			System.out.println("Key '" + input + "' not found in userconfig");
		}
		return out;
	}

	
	public static JSONObject getUserObject(String key) {
		return userConfigJSON.getJSONObject(key);
	}
	
	
	public static void setUserObject(String key, JSONObject value) {
		userConfigJSON.setJSONObject(key, value);
	}
	
	
	public static int getColor(String input) {
		return (int) Long.parseLong(configJSON.getString(input), 16);
	}
	
	
	public static void saveUserConfig() {
		p.saveJSONObject(userConfigJSON, "config_user.json");
	}
}

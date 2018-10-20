package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

import main.Server;

public class Setting {
	
	private Properties prop;
	private static final String RELATIVE_URL = "./setting.ini";
	
	private Setting() {
		prop = loadSetting(RELATIVE_URL);
	}
	
	private static class Singleton {
		private static final Setting instance = new Setting();
	}
	
	public static Setting getInstance() {
		return Singleton.instance;
	}
	
	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}
	
	public Properties reloadProp() {
		prop = loadSetting(RELATIVE_URL);
		return prop;
	}

	private Properties loadSetting(String path) {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(new File(path)));
			
			return prop;
		} catch (IOException e) {
			System.out.println("Unavailable to get setting. Create new one.");
			return createSetting();
		}
	}
	
	private Properties createSetting() {
		try {
			Properties prop = new Properties();
			prop.setProperty("port", String.valueOf(Server.PORT));
			prop.setProperty("password", "");
			prop.setProperty("allowCharacter", "false");
			prop.store(new OutputStreamWriter(
				    new FileOutputStream("setting.ini"), "UTF-8"), 
				    null);
			
			return prop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

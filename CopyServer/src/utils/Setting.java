package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class Setting {
	
	private final String explain = "password : 빈칸 시 인증 없이 연결\nallowCharacter : 숫자 외 문자 허용 여부";
	private Properties prop;
	
	private Setting() {
		prop = loadSetting("./setting.ini");
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

	private Properties loadSetting(String path) {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(new File(path)));
			
			return prop;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return createSetting();
		}
	}
	
	private Properties createSetting() {
		try {
			Properties prop = new Properties();
			prop.setProperty("password", "");
			prop.setProperty("allowCharacter", "false");
			prop.store(new OutputStreamWriter(
				    new FileOutputStream("setting.ini"), "UTF-8"), 
				    explain);
			
			return prop;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}

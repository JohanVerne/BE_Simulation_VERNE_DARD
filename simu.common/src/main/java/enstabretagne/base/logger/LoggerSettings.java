/*
 * 
 */
package enstabretagne.base.logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import enstabretagne.base.Settings;
import enstabretagne.base.messages.MessagesLogger;

// TODO: Auto-generated Javadoc
/**
 * The Class LoggerSettings.
 */
public class LoggerSettings {
	
	/** The settings. */
	public static LoggerSettings settings ;
	
	/** The Clear all before. */
	public boolean ClearAllBefore;
	
	/** The Constant loggerSettingsFileName. */
	private static final String loggerSettingsFileName = "logger_settings.json";
	static {
		File dir= new File(System.getProperty("user.dir")+File.separator+ Settings.configuration_dir);
		if(!dir.exists()) {
			System.err.println(MessagesLogger.LoggerSettingsDirNotFound + ":" + dir.getAbsolutePath());
			System.exit(1);
		}
		File f= new File(dir.getAbsoluteFile()+File.separator+loggerSettingsFileName);
		if(!f.exists()) {
			System.err.println(MessagesLogger.LoggerSettingsFileNotFound+ ":" + f.getAbsolutePath());
			System.exit(1);
		}
		
		try {
			String content;
			content = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
			settings = (LoggerSettings) Settings.fromJson(content,LoggerSettings.class);

		} catch (Exception e) {
			System.err.println(MessagesLogger.LoggerParsingFailed + " : " +e.getMessage());
			System.exit(1);
//			e.printStackTrace();
		}
			
	}
	
	/**
	 * Instantiates a new logger settings.
	 */
	public LoggerSettings() {
		classToFilter = new ArrayList<String>();
		loggerConfs = new ArrayList<LoggerConf>();
		classToFilter = new ArrayList<String>();
		levelsToRecord = new ArrayList<String>();
	}
	
	/** The logger confs. */
	public List<LoggerConf> loggerConfs;
	
	/** The class to filter. */
	public List<String> classToFilter;

	/** The levels to record. */
	public List<String> levelsToRecord;
}

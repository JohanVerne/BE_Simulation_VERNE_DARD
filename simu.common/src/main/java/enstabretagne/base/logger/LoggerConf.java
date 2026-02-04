/*
 * 
 */
package enstabretagne.base.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import enstabretagne.base.messages.MessagesLogger;

// TODO: Auto-generated Javadoc
/**
 * The Class LoggerConf.
 */
public class LoggerConf {
	
	/** The parametres. */
	public HashMap<String, String> parametres;
	
	public boolean activate;
	/**
	 * Instantiates a new logger conf.
	 */
	public LoggerConf() {
		parametres = new HashMap<String, String>();
		classToFilter=new ArrayList<>();
		levelsToRecord = new ArrayList<String>();
	}

	/**
	 * Check logger conf.
	 *
	 * @return the string
	 */
	public String checkLoggerConf() {
		String result = "";
		if(!parametres.containsKey(LoggerParamsNames.LoggerKind.toString()))
				result = MessagesLogger.LoggerKindNotProvided;
		else {
			String classToFind = parametres.get(LoggerParamsNames.LoggerKind.toString());
			try {
				Class.forName(classToFind);
			} catch (ClassNotFoundException e) {
				result = MessagesLogger.LoggerKindNotFound + " : "+classToFind;
			}
		}
		return result;
	}

	/** The class to filter. */
	public List<String> classToFilter;

	/** The levels to record. */
	public List<String> levelsToRecord;

}

/**
* Classe LoggerParamsNames.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.logger;

// TODO: Auto-generated Javadoc
/**
 * The Enum LoggerParamsNames.
 */
public enum LoggerParamsNames {
	
	/** The File name. */
	FileName("FileName"),
	
	/** The Logger kind. */
	LoggerKind("LoggerKind"),
	
	/** The Directory name. */
	DirectoryName("DirectoryName"),
	
	/** The Record start time. */
	RecordStartTime("RecordStartTime");
	
	/** The param name. */
	private String paramName;
	
	/**
	 * Instantiates a new logger params names.
	 *
	 * @param paramName the param name
	 */
	private LoggerParamsNames(String paramName){
		this.paramName = paramName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return paramName;
	}

}


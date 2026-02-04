/**
* Classe LogLevels.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.logger;

// TODO: Auto-generated Javadoc
/**
 * The Enum LogLevels.
 */
public enum LogLevels {
	
	/** The warning. */
	warning("Warning"),
	
	/** The information. */
	information("Information"),
	
	/** The error. */
	error("Error"),
	
	/** The detail. */
	detail("Detail"),
	
	/** The fatal. */
	fatal("Fatal"),
	
	/** The data. */
	data("Data"),
	
	/** The data recordable. */
	dataRecordable("DataRecordable"),
	
	/** The data simple. */
	dataSimple("DataSimple");
	
	/** The s. */
	String s;
	
	/**
	 * Instantiates a new log levels.
	 *
	 * @param s the s
	 */
	private LogLevels(String s)
	{
		this.s=s;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return s;
	}
}


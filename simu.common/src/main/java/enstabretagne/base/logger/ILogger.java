/**
* Classe ILogger.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.logger;

import java.time.temporal.Temporal;
import java.util.List;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.simulation.basics.ScenarioId;

// TODO: Auto-generated Javadoc
/**
 * The Interface ILogger.
 */
public interface ILogger {
	
	/**
	 * Check logger conf.
	 *
	 * @param conf the conf
	 * @return the string
	 */
	String checkLoggerConf(LoggerConf conf);
	
	/**
	 * Open.
	 *
	 * @param conf the conf
	 * @return true, if successful
	 */
	boolean open(LoggerConf conf);
	
	/**
	 * Log.
	 *
	 * @param el the el
	 * @param scenarioId the scenario id
	 * @param t the t
	 * @param d the d
	 * @param level the level
	 * @param obj the obj
	 * @param function the function
	 * @param message the message
	 * @param args the args
	 */
	void log(StackTraceElement el,ScenarioId scenarioId,Temporal t, LogicalDateTime d, LogLevels level, Object obj,String function,String message,Object... args);
	
	void save();
	
	List<String> getlogSummary();
	/**
	 * Close.
	 */
	void close();
	
	/**
	 * Clear.
	 *
	 * @param conf the conf
	 */
	void clear(LoggerConf conf);
}


/*
 * 
 */
package enstabretagne.base.logger.loggerimpl;

import java.util.ArrayList;
import java.util.List;

import enstabretagne.base.logger.ILogger;
import enstabretagne.base.logger.LoggerConf;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractLogger.
 */
public abstract class AbstractLogger implements ILogger {

	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.ILogger#checkLoggerConf(enstabretagne.base.logger.loggerimpl.LoggerConf)
	 */
	@Override
	public String checkLoggerConf(LoggerConf conf) {
		
		return conf.checkLoggerConf();
	}

	public AbstractLogger() {
		logSummary = new ArrayList<String>();
	}
	protected List<String> logSummary;
	@Override
	public List<String> getlogSummary() {
		return logSummary;
	}

}

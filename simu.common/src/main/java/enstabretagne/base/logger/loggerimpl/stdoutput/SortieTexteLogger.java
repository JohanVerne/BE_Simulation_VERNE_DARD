/**
* Classe SysOutLogger.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.logger.loggerimpl.stdoutput;

import java.time.temporal.Temporal;

import enstabretagne.base.Settings;
import enstabretagne.base.logger.IRecordable;
import enstabretagne.base.logger.LogLevels;
import enstabretagne.base.logger.LoggerConf;
import enstabretagne.base.logger.LoggerParamsNames;
import enstabretagne.base.logger.loggerimpl.AbstractLogger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.simulation.basics.ScenarioId;

// TODO: Auto-generated Javadoc
/**
 * The Class SortieTexteLogger.
 */
public class SortieTexteLogger extends AbstractLogger {
	
	/** The start record time. */
	LogicalDateTime startRecordTime;

	/** The sep. */
	char sep = Settings.sep();
	
	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.ILogger#open(enstabretagne.base.logger.loggerimpl.LoggerConf)
	 */
	@Override
	public boolean open(LoggerConf conf) {
		if(conf.parametres.containsKey(LoggerParamsNames.RecordStartTime.toString())){
	    	startRecordTime = LogicalDateTime.LogicalDateFrom(conf.parametres.get(LoggerParamsNames.RecordStartTime.toString()));
	    	if(startRecordTime==null)
	    		System.exit(1);
	    }

		return true;
	}
	

	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.ILogger#log(java.lang.StackTraceElement, enstabretagne.simulation.components.ScenarioId, java.time.temporal.Temporal, enstabretagne.base.time.LogicalDateTime, enstabretagne.base.logger.LogLevels, java.lang.Object, java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void log(StackTraceElement el,ScenarioId scenarioId,Temporal t, LogicalDateTime d, LogLevels level, Object obj,
			String function, String message, Object... args) {		
//		StackTraceElement[]sts = Thread.currentThread().getStackTrace();
//		StackTraceElement el= sts[8];
		if(startRecordTime!=null && d!=null && d.compareTo(startRecordTime)<0)
			return;

		
		String elTxt = "("+el.getFileName()+":"+el.getLineNumber()+")>"+el.getMethodName();
		
		if(args!=null) {
			for(Object arg : args){
				if(Exception.class.isAssignableFrom(arg.getClass()))
					((Exception) arg).printStackTrace(System.err);;
			}
		}
		String logicalDate;
		if(d==null)
			logicalDate ="pas de temps logique";
		else
			logicalDate = "" + d;
			
		if(obj!=null)
		{
			if(level.equals(LogLevels.dataRecordable) || level.equals(LogLevels.data)){
				IRecordable r = (IRecordable) obj;
				String s="";
				for(int i=0;i<r.getTitles().length;i++){
					s=s+r.getTitles()[i]+"="+r.getRecords()[i]+";";
					
				}
				System.out.println(String.format(elTxt+sep+ scenarioId.getScenarioId()+sep+Long.toString(scenarioId.getRepliqueNumber())+sep+logicalDate + sep + level + sep + s + sep+message,args));
			}
			else if(level.equals(LogLevels.error) || level.equals(LogLevels.fatal))
			{
				System.err.println(String.format(elTxt+sep+ scenarioId.getScenarioId()+sep+Long.toString(scenarioId.getRepliqueNumber())+sep+logicalDate + sep + level + sep + obj.toString() + sep+message,args));				
			}
			else
				System.out.println(String.format(elTxt+sep+ scenarioId.getScenarioId()+sep+Long.toString(scenarioId.getRepliqueNumber())+sep+logicalDate + sep + level + sep + obj.toString() + sep+message,args));
		}
		else {
			if(level.equals(LogLevels.error) || level.equals(LogLevels.fatal))
			{
				System.err.println(String.format(elTxt+sep+ scenarioId.getScenarioId()+sep+Long.toString(scenarioId.getRepliqueNumber())+sep+logicalDate + sep + level + sep + sep+message,args));				
			}
			else
			{			
			System.out.println(String.format(elTxt+sep+ scenarioId.getScenarioId()+sep+Long.toString(scenarioId.getRepliqueNumber())+sep+logicalDate + sep + level + sep + sep+message,args));
			}
		}
	}

	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.ILogger#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.ILogger#clear(enstabretagne.base.logger.loggerimpl.LoggerConf)
	 */
	@Override
	public void clear(LoggerConf conf) {
		System.out.flush();
		System.err.flush();
		
	}


	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.loggerimpl.AbstractLogger#checkLoggerConf(enstabretagne.base.logger.loggerimpl.LoggerConf)
	 */
	@Override
	public String checkLoggerConf(LoggerConf conf) {
		
		return super.checkLoggerConf(conf);
	}


	@Override
	public void save() {
		
	}



}


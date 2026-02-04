/**
* Classe Logger.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import enstabretagne.base.Settings;
import enstabretagne.base.logger.loggerimpl.ObjectAnalyseForLog;
import enstabretagne.base.logger.loggerimpl.TypeAnalyseForLog;
import enstabretagne.base.messages.MessagesLogger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.simulation.basics.IScenarioIdProvider;
import enstabretagne.simulation.basics.ScenarioId;
import enstabretagne.simulation.basics.ISimulationDateProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class Logger.
 */
/*
 * La classe Logger permet d'enregistrer les journaux et les donn�es produites
 * au cours de la simulation. Les m�thodes publique Detail, Error, Fatal,
 * Information ajoutent d'elle m�me un timestamp li� au temps r�el Il d�livre
 * plusieurs services: -
 */
public class Logger {

	/** The log. */
	static boolean terminated = false;
	private static Logger log;
	static {
		log = new Logger();
		log.Init();

	}
	
	public static Path convRightPath(String myPath) {
		boolean isLinux = (File.separatorChar == '/');
		if(isLinux) myPath = myPath.replace("\\", "/");
		else myPath = myPath.replace("/", "\\");
		
		return Paths.get(myPath).normalize();
	}

	// permet de charger la classe
	public static void load() {
	}

	/** The simulation date provider. */
	private ISimulationDateProvider simulationDateProvider;

	private IScenarioIdProvider scenarioIDProvider;

	// --------------------------- Journaling
	// -----------------------------------
	/// <summary>
	/// Link between simulation application overridable settings for trace level
	// and the 'old' TraceSwitch
	/** The loggers. */
	/// </summary>
	private List<ILogger> loggers;
	

	/**
	 * Data simple.
	 *
	 * @param classement the classement
	 * @param obj        the obj
	 */
	public static void DataSimple(String classement, Object... obj) {
		if (obj != null) {
			log.log(LogLevels.dataSimple, classement, "", "", obj);
		}

	}

	/**
	 * Data.
	 *
	 * @param obj the obj
	 */
	public static void Data(Object obj) {
		if (obj != null) {
			if (IRecordable.class.isAssignableFrom(obj.getClass())) {
				log.log(LogLevels.dataRecordable, obj, "", "");
			} else
				log.log(LogLevels.data, obj, "", "");
		}
	}

	/**
	 * Fatal.
	 *
	 * @param obj      the obj
	 * @param function the function
	 * @param message  the message
	 * @param args     the args
	 */
	public static void Fatal(Object obj, String function, String message, Object... args) {
		log.log(LogLevels.fatal, obj, function, message, args);
		Logger.Terminate();
		System.exit(1);
	}

	/**
	 * Error.
	 *
	 * @param obj      the obj
	 * @param function the function
	 * @param message  the message
	 * @param args     the args
	 */
	public static void Error(Object obj, String function, String message, Object... args) {
		log.log(LogLevels.error, obj, function, message, args);
	}

	/**
	 * Detail.
	 *
	 * @param obj      the obj
	 * @param function the function
	 * @param message  the message
	 * @param args     the args
	 */
	public static void Detail(Object obj, String function, String message, Object... args) {
		log.log(LogLevels.detail, obj, function, message, args);
	}

	/**
	 * Information.
	 *
	 * @param obj      the obj
	 * @param function the function
	 * @param message  the message
	 * @param args     the args
	 */
	public static void Information(Object obj, String function, String message, Object... args) {
		log.log(LogLevels.information, obj, function, message, args);
	}

	/**
	 * Warning.
	 *
	 * @param obj      the obj
	 * @param function the function
	 * @param message  the message
	 * @param args     the args
	 */
	public static void Warning(Object obj, String function, String message, Object... args) {
		log.log(LogLevels.warning, obj, function, message, args);
	}

	/**
	 * Log.
	 *
	 * @param level    the level
	 * @param obj      the obj
	 * @param function the function
	 * @param message  the message
	 * @param args     the args
	 */
	private void log(LogLevels level, Object obj, String function, String message, Object... args) {
		if (simulationDateProvider != null) {
			if (scenarioIDProvider != null && scenarioIDProvider.getScenarioId() != null)
				log(scenarioIDProvider.getScenarioId(), Instant.now(), simulationDateProvider.SimulationDate(), level,
						obj, function, message, args);
			else
				log(ScenarioId.ScenarioID_NULL, Instant.now(), simulationDateProvider.SimulationDate(), level, obj,
						function, message, args);
		} else
			log(ScenarioId.ScenarioID_NULL, Instant.now(), LogicalDateTime.Zero, level, obj, function, message, args);

	}

	/** The first title data simple. */
	HashMap<String, String[]> firstTitleDataSimple = null;

	boolean mustBeFiltered(boolean isActive, Object obj, StackTraceElement el, LogLevels level,
			List<String> levelsToRecord, List<String> classesToFilter) {

		if (isActive == false)
			return true;

		String s1 = "";
		if (obj != null)
			s1 = obj.getClass().getName();
		String s2 = "";
		if (el != null)
			s2 = el.getClassName();

		boolean hasTBeFiltered = false;
		for (String s : classesToFilter)
			hasTBeFiltered = hasTBeFiltered | s1.compareToIgnoreCase(s) == 0 | s2.compareToIgnoreCase(s) == 0;

		if (!levelsToRecord.contains(level.s))
			hasTBeFiltered = true;
		return hasTBeFiltered;
	}

	/**
	 * Log.
	 *
	 * @param scenarioId the scenario id
	 * @param t          the t
	 * @param d          the d
	 * @param level      the level
	 * @param obj        the obj
	 * @param function   the function
	 * @param message    the message
	 * @param args       the args
	 */
	protected void log(ScenarioId scenarioId, Temporal t, LogicalDateTime d, LogLevels level, Object obj,
			String function, String message, Object... args) {
		boolean toBeLogged = true;
		StackTraceElement el = Thread.currentThread().getStackTrace()[4];

		if (obj != null && Settings.filterEngineLogs()) {

			boolean hasTBeFiltered = mustBeFiltered(true, obj, el, level, LoggerSettings.settings.levelsToRecord,
					LoggerSettings.settings.classToFilter);
//			boolean hasTBeFiltered = false;
//			for (String s : LoggerSettings.settings.classToFilter)
//				hasTBeFiltered = hasTBeFiltered | s1.compareToIgnoreCase(s) == 0 | s2.compareToIgnoreCase(s) == 0;
//
//			if(!LoggerSettings.settings.levelsToRecord.contains(level.s))
//				hasTBeFiltered=true;

			if (hasTBeFiltered)
				if (level.equals(LogLevels.detail) || level.equals(LogLevels.information)
						|| level.equals(LogLevels.data))
					toBeLogged = false;
		}

		if (toBeLogged) {

			if (level.equals(LogLevels.data)) {
				ObjectAnalyseForLog objAbilities = analyseObject(obj);
				for (int i = 0; i < loggers.size(); i++) {
					boolean hasTBeFiltered = mustBeFiltered(loggerConfs.get(i).activate, obj, el, level,
							loggerConfs.get(i).levelsToRecord, loggerConfs.get(i).classToFilter);

					if (!hasTBeFiltered)
						loggers.get(i).log(el, scenarioId, t, d, level, objAbilities, function, message, args);
				}
			} else if (level.equals(LogLevels.dataSimple)) {
				if (!firstTitleDataSimple.containsKey(obj.toString()))
					firstTitleDataSimple.put(obj.toString(), Arrays.asList(args).stream().map(tit -> tit.toString())
							.collect(Collectors.toList()).toArray(new String[0]));
				else {
					String[] tit = firstTitleDataSimple.get(obj.toString());
					IRecordable data = new IRecordable() {

						@Override
						public String[] getTitles() {
							return tit;
						}

						@Override
						public String[] getRecords() {
							return Arrays.asList(args).stream().map(field -> field.toString())
									.collect(Collectors.toList()).toArray(new String[0]);
						}

						@Override
						public String getClassement() {
							return obj.toString();
						}
					};
					for (int i = 0; i < loggers.size(); i++) {
						boolean hasTBeFiltered = mustBeFiltered(loggerConfs.get(i).activate, obj, el, level,
								loggerConfs.get(i).levelsToRecord, loggerConfs.get(i).classToFilter);

						if (!hasTBeFiltered)
							loggers.get(i).log(el, scenarioId, t, d, level, data, function, message, new Object[0]);
					}

				}
			} else if (level.equals(LogLevels.dataRecordable)) {
				for (int i = 0; i < loggers.size(); i++) {
					boolean hasTBeFiltered = mustBeFiltered(loggerConfs.get(i).activate, obj, el, level,
							loggerConfs.get(i).levelsToRecord, loggerConfs.get(i).classToFilter);

					if (!hasTBeFiltered)
						loggers.get(i).log(el, scenarioId, t, d, level, obj, function, message, new Object[0]);
				}
			} else {
				for (int i = 0; i < loggers.size(); i++) {
					boolean hasTBeFiltered = mustBeFiltered(loggerConfs.get(i).activate, obj, el, level,
							loggerConfs.get(i).levelsToRecord, loggerConfs.get(i).classToFilter);

					if (!hasTBeFiltered)
						loggers.get(i).log(el, scenarioId, t, d, level, obj, function, message, args);
				}

			}

		}
	}

	/** The data log abilities. */
	private HashMap<Class<?>, TypeAnalyseForLog> dataLogAbilities;

	/**
	 * Analyse object.
	 *
	 * @param o the o
	 * @return the object analyse for log
	 */
	private ObjectAnalyseForLog analyseObject(Object o) {
		if (o == null) {
			return null;
		}
		if (!dataLogAbilities.containsKey(o.getClass())) {

			ToRecord a = o.getClass().getAnnotation(ToRecord.class);
			HashMap<String, Method> mL = new HashMap<String, Method>();
			if (a != null) {
				System.out.println(a.name());

				for (Method m : o.getClass().getMethods()) {
					ToRecord ma = m.getAnnotation(ToRecord.class);
					if (ma != null) {
						if (m.getParameterCount() > 0 || m.getReturnType().equals(Void.TYPE)) {
							System.err.println(MessagesLogger.LoggerDataIncorrectMethod + " : " + m.getName());
							System.exit(1);
						}
						if (mL.containsKey(ma.name())) {
							System.err.println(MessagesLogger.LoggerDataReuseOfAToRecordName + " : '" + ma.name()
									+ "' trouv� sur la m�thode '" + m.getName() + "'"
									+ " d�j� pr�sente sur la m�thode '" + mL.get(ma.name()) + "'" + " dans la classe '"
									+ o.getClass().getName() + "'");
							System.exit(1);
						}
						mL.put(ma.name(), m);
						System.out.println(ma.name());
					}
				}
				TypeAnalyseForLog tpLog = new TypeAnalyseForLog(o.getClass(), a.name(), mL);
				ObjectAnalyseForLog oa4l = new ObjectAnalyseForLog(tpLog, o);
				dataLogAbilities.put(o.getClass(), tpLog);
				return oa4l;
			} else {
				TypeAnalyseForLog tpLog = new TypeAnalyseForLog(o.getClass(), o.getClass().getSimpleName(), null);
				return new ObjectAnalyseForLog(tpLog, o);
			}
		} else {
			TypeAnalyseForLog tpLog = dataLogAbilities.get(o.getClass());
			return new ObjectAnalyseForLog(tpLog, o);
		}

	}

	/**
	 * Adds the logger.
	 *
	 * @param log the log
	 */
	public void addLogger(ILogger log) {
		loggers.add(log);
	}

	/**
	 * Clear loggers.
	 */
	public void clearLoggers() {
		loggers.clear();
	}

	/**
	 * Instantiates a new logger.
	 */
	private Logger() {
		loggers = new ArrayList<ILogger>();
		dataLogAbilities = new HashMap<Class<?>, TypeAnalyseForLog>();
		firstTitleDataSimple = new HashMap<String, String[]>();
		loggerConfs = new ArrayList<>();
		
	}

	/**
	 * Checks if is initialized.
	 *
	 * @return true, if is initialized
	 */
	public static boolean isInitialized() {
		return log.simulationDateProvider != null & log.loggers.size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	public void finalize() throws Throwable {
//		if(!terminated) {
//			super.finalize();
//			loggers.forEach((log) -> log.close());
//			terminated=true;
//		}
	}

	/**
	 * Sets the date provider.
	 *
	 * @param e the new date provider
	 */
	public static void setDateProvider(ISimulationDateProvider e) {
		log.simulationDateProvider = e;
	}

	/**
	 * Inits the.
	 */
	public void Init() {
		terminated = false;
		log.clearLoggers();
		if (log.simulationDateProvider == null)
			log.simulationDateProvider = new ISimulationDateProvider() {

				@Override
				public LogicalDateTime SimulationDate() {

					return new LogicalDateTime("01/01/2015 00:00:00");
				}
			};

		int i = 0;
		for (LoggerConf lc : LoggerSettings.settings.loggerConfs) {
			i++;
			if (lc.checkLoggerConf().equals("")) {
				if (lc.activate) {
					String s = lc.parametres.get(LoggerParamsNames.LoggerKind.toString());
					try {

						Class<?> c = Class.forName(s);
						if (ILogger.class.isAssignableFrom(c)) {
							try {
								ILogger logger = (ILogger) c.getDeclaredConstructor().newInstance();
								boolean success = logger.open(lc);
								if (success) {
									log.addLogger(logger);
									log.addConf(lc);
								}

								if (LoggerSettings.settings.ClearAllBefore)
									logger.clear(lc);
							} catch (InstantiationException | IllegalAccessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} catch (ClassNotFoundException e1) {
						System.err.println("Attention la classe de logger '" + e1.getMessage()
								+ "' n'a pas �t� trouv�e. Logger non pris en charge ");
					}
				}
			} else {
				System.err.println("LoggerConfs N�" + i + " : " + lc.checkLoggerConf());
				System.exit(1);
			}
		}

	}

	List<LoggerConf> loggerConfs;

	private void addConf(LoggerConf lc) {
		loggerConfs.add(lc);
	}

	/**
	 * Sets the simulation date provider.
	 *
	 * @param e the new simulation date provider
	 */
	public void setSimulationDateProvider(ISimulationDateProvider e) {
		log.simulationDateProvider = e;

	}

	public static void setScenarioIdProvider(IScenarioIdProvider idProv) {
		log.scenarioIDProvider = idProv;
	}

	public static void SaveAndContinue() {
		log.save();
	}

	public void save() {
		loggers.forEach((log) -> log.save());
	}

	public static void CloseAndReinit() {
		Terminate();
		log = new Logger();
		log.Init();
	}

	private void displayResume() {
		System.out.println();
		System.out.println("======");

		System.out.println("Journal des loggers");
		System.out.println();
		
		for(var log : loggers)
		{
			System.out.println(log.getClass().getSimpleName());
			for(var s : log.getlogSummary()) {
				System.out.println(">> " + s);
			}
			System.out.println("-----");
			System.out.println("");
		}
	}
	public void close() {
		displayResume();
			
		loggers.forEach((log) -> log.close());
		
	}

	/**
	 * Terminate.
	 */
	public static void Terminate() {
		try {
			log.close();
			log.finalize();


		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

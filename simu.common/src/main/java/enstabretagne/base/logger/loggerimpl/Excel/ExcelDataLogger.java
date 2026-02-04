/**
* Classe ExcelDataloggerImpl.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.logger.loggerimpl.Excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import enstabretagne.base.logger.IRecordable;
import enstabretagne.base.logger.LogLevels;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.logger.LoggerConf;
import enstabretagne.base.logger.LoggerParamsNames;
import enstabretagne.base.logger.loggerimpl.AbstractLogger;
import enstabretagne.base.messages.MessagesLogger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.simulation.basics.ScenarioId;

// TODO: Auto-generated Javadoc
/**
 * The Class ExcelDataLogger.
 */
public class ExcelDataLogger extends AbstractLogger {

	/**
	 * The Class Logs.
	 */
	class Logs implements IRecordable {

		/** The s. */
		String[] s;

		/**
		 * Instantiates a new logs.
		 *
		 * @param s the s
		 */
		public Logs(String[] s) {
			this.s = s;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see enstabretagne.base.logger.IRecordable#getTitles()
		 */
		@Override
		public String[] getTitles() {
			String[] h = { "Scenario", "Replique", "Temps Reel", "Temps Logique", "Niveau de Log", "Nom Objet",
					"Fonction", "Message" };
			return h;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see enstabretagne.base.logger.IRecordable#getRecords()
		 */
		@Override
		public String[] getRecords() {
			return s;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see enstabretagne.base.logger.IRecordable#getClassement()
		 */
		@Override
		public String getClassement() {
			return "Logs";
		}
	}

	/**
	 * The Class DataLogs.
	 */
	class DataLogs implements IRecordable {

		/** The s. */
		IRecordable s;

		/** The temps reel. */
		String tempsReel;

		/** The tempslogique. */
		String tempslogique;

		/** The nom obj. */
		String nomObj;

		/** The scenario id. */
		ScenarioId scenarioId;

		/**
		 * Instantiates a new data logs.
		 *
		 * @param scenarioId   the scenario id
		 * @param tempsReel    the temps reel
		 * @param tempslogique the tempslogique
		 * @param nomObj       the nom obj
		 * @param s            the s
		 */
		public DataLogs(ScenarioId scenarioId, String tempsReel, String tempslogique, String nomObj, IRecordable s) {
			this.s = s;
			this.tempsReel = tempsReel;
			this.tempslogique = tempslogique;
			this.nomObj = nomObj;
			this.scenarioId = scenarioId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see enstabretagne.base.logger.IRecordable#getTitles()
		 */
		@Override
		public String[] getTitles() {
			String[] h = { "Scenario", "Replique", "Temps Reel", "Temps Logique", "Nom Objet" };
			if (s != null)
				return join(h, s.getTitles());
			else
				return h;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see enstabretagne.base.logger.IRecordable#getRecords()
		 */
		@Override
		public String[] getRecords() {
			String[] r = { scenarioId.getScenarioId(), Long.toString(scenarioId.getRepliqueNumber()), tempsReel,
					tempslogique, nomObj };
			if (s != null)
				return join(r, s.getRecords());
			else
				return r;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see enstabretagne.base.logger.IRecordable#getClassement()
		 */
		@Override
		public String getClassement() {
			return s.getClassement();
		}
	}

	/**
	 * Join.
	 *
	 * @param parms the parms
	 * @return the string[]
	 */
	private String[] join(String[]... parms) {
		// calculate size of target array
		int size = 0;
		for (String[] array : parms) {
			size += array.length;
		}

		String[] result = new String[size];

		int j = 0;
		for (String[] array : parms) {
			for (String s : array) {
				result[j++] = s;
			}
		}
		return result;
	}

	/** The dico locations. */
	HashMap<String, Integer> dicoLocations;

	/** The wb. */
	SXSSFWorkbook wb;

	/** The start record time. */
	LogicalDateTime startRecordTime;

	/** The ps. */
	PrintStream ps;

	/** The baos. */
	ByteArrayOutputStream baos;

	/** The file out. */
	FileOutputStream fileOut;

	boolean isWbOpened;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * enstabretagne.base.logger.ILogger#open(enstabretagne.base.logger.loggerimpl.
	 * LoggerConf)
	 */
	@Override
	public boolean open(LoggerConf conf) {
		wb = new SXSSFWorkbook(10000);
		isWbOpened = true;
		dicoLocations = new HashMap<>();
		baos = new ByteArrayOutputStream();
		ps = new PrintStream(baos);

		if (conf.parametres.containsKey(LoggerParamsNames.RecordStartTime.toString())) {
			startRecordTime = LogicalDateTime
					.LogicalDateFrom(conf.parametres.get(LoggerParamsNames.RecordStartTime.toString()));
			if (startRecordTime == null)
				System.exit(1);
		}

		boolean success = true;
		String dirName;
		String fileName;
		

		 
		if (conf.parametres.containsKey(LoggerParamsNames.DirectoryName.toString())) {
			dirName = conf.parametres.get(LoggerParamsNames.DirectoryName.toString()).toString();
			if(dirName.startsWith("~")) {
				dirName = System.getProperty("user.home").concat(File.separator).concat(dirName.substring(1));
			}
			Path myPath = Logger.convRightPath(dirName);
			if(myPath.isAbsolute())
				dirName = myPath.toString();			
			else {
				Path userDir = Logger.convRightPath(System.getProperty("user.dir"));
				dirName = Paths.get(userDir.toString()+File.separator+myPath.toString()).normalize().toString();
			}

			
		} else
			dirName = System.getProperty("user.dir");
		if (conf.parametres.containsKey(LoggerParamsNames.FileName.toString())) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_ssmmHH");  
			LocalDateTime now = LocalDateTime.now();  
			   
			fileName = dtf.format(now)+ "_"
					+ conf.parametres.get(LoggerParamsNames.FileName.toString()).toString();

		} else
			fileName = "monfichier.xls";


		File directory = new File(dirName);
		boolean res = directory.mkdirs();

		try {
			String fullFileName = dirName + File.separator + fileName;
			logSummary.add(this.getClass().getSimpleName() + ">>"+ fullFileName);

			fileOut = new FileOutputStream(fullFileName);

		} catch (FileNotFoundException e) {
			success = false;
			System.err.println("Logger " + this.getClass().getCanonicalName() + " n'a pu �tre cr��.)");
			System.err.println(dirName + "\\" + fileName
					+ " est sans doute ouvert ou n'existe pas (chemin non existant au pr�alable par exemple)");
		}
		
		if(!success) logSummary.add("Erreur dans la tentative de création de " + dirName);

		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see enstabretagne.base.logger.ILogger#log(java.lang.StackTraceElement,
	 * enstabretagne.simulation.components.ScenarioId, java.time.temporal.Temporal,
	 * enstabretagne.base.time.LogicalDateTime, enstabretagne.base.logger.LogLevels,
	 * java.lang.Object, java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void log(StackTraceElement el, ScenarioId scenarioId, Temporal t, LogicalDateTime d, LogLevels level,
			Object obj, String function, String message, Object... args) {

		if (startRecordTime != null && d.compareTo(startRecordTime) < 0)
			return;
		if (!level.equals(LogLevels.dataRecordable) && !level.equals(LogLevels.data)
				&& !level.equals(LogLevels.dataSimple)) {
			String elTxt = "(" + el.getFileName() + ":" + el.getLineNumber() + ")>" + el.getMethodName();

			ps.printf(message, args);
			String[] s;
			if (d == null) {
				if (obj != null)
					s = new String[] { scenarioId.getScenarioId(), Long.toString(scenarioId.getRepliqueNumber()),
							t.toString(), "", level.toString(), obj.toString(), elTxt, baos.toString() };
				else
					s = new String[] { scenarioId.getScenarioId(), Long.toString(scenarioId.getRepliqueNumber()),
							t.toString(), "", level.toString(), "", elTxt, baos.toString() };
			} else {
				if (obj != null)
					s = new String[] { scenarioId.getScenarioId(), Long.toString(scenarioId.getRepliqueNumber()),
							t.toString(), d.toString(), level.toString(), obj.toString(), elTxt, baos.toString() };
				else
					s = new String[] { scenarioId.getScenarioId(), Long.toString(scenarioId.getRepliqueNumber()),
							t.toString(), d.toString(), level.toString(), "", elTxt, baos.toString() };
			}
			baos.reset();

			Logs l = new Logs(s);
			createRowFrom(l);
		} else if (level.equals(LogLevels.dataRecordable) || level.equals(LogLevels.data)
				|| level.equals(LogLevels.dataSimple)) {
			DataLogs dl;
			if (obj != null) {
				if (d != null) {
					if (IRecordable.class.isAssignableFrom(obj.getClass()))
						dl = new DataLogs(scenarioId, t.toString(), d.toString(), obj.toString(), (IRecordable) obj);
					else
						dl = new DataLogs(scenarioId, t.toString(), d.toString(), obj.toString(), null);
				} else {
					if (IRecordable.class.isAssignableFrom(obj.getClass()))
						dl = new DataLogs(scenarioId, t.toString(), "", obj.toString(), (IRecordable) obj);
					else
						dl = new DataLogs(scenarioId, t.toString(), "", obj.toString(), null);

				}

			} else {
				dl = new DataLogs(scenarioId, t.toString(), d.toString(), "SANS OBJET", null);
			}
			createRowFrom(dl);

		} else {
			System.err.println(MessagesLogger.LoggerImpossible + " ExcelDatalogger");
			System.exit(1);
		}
	}

	public synchronized void initsave() {
		try {
			int nbS = wb.getNumberOfSheets();
			
			if (nbS != 0) {
				for (int i = 0; i < nbS; i++) {
					Sheet s = wb.getSheetAt(i);
					if (s.getRow(0) != null) {// ceci arrive si on vide la m�moire tampon pour les grands fichiers. Dans
												// ce cas pas de possibilit� de traiter la mise en page de gros fichiers
	
						Cell firstCell = s.getRow(0).getCell(0);
						
						Cell lastCell = s.getRow(0).getCell((int) s.getRow(0).getLastCellNum()-1);
						s.setAutoFilter(new CellRangeAddress(firstCell.getRowIndex(), lastCell.getRowIndex(),
								lastCell.getRowIndex(), lastCell.getColumnIndex()));
					}
				}
			}
			else { //comme aucune feuille n'a �t� cr��e, si on enregistre ainsi le classeur, il sera corrompu.
				//on cr�e donc une page vide.
				Sheet s = wb.createSheet("Vide");
				s.createRow(1);
			}

			fileOut.flush();

			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see enstabretagne.base.logger.ILogger#close()
	 */
	@Override
	public synchronized void close() {
		if (isWbOpened) {
			try {

				initsave();

				if (fileOut.getChannel().isOpen())
					wb.write(fileOut);
				wb.close();
				isWbOpened = false;
				logSummary.clear();
				fileOut.flush();

				fileOut.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * enstabretagne.base.logger.ILogger#clear(enstabretagne.base.logger.loggerimpl.
	 * LoggerConf)
	 */
	@Override
	public void clear(LoggerConf conf) {

	}

	/**
	 * Fill row with.
	 *
	 * @param r    the r
	 * @param data the data
	 */
	private void fillRowWith(Row r, String[] data) {
		Cell c = null;

		for (String s : data) {
			if (c == null)
				c = r.createCell(0);
			else
				c = r.createCell(c.getColumnIndex() + 1);

			try {
				double d = Double.valueOf(s);
				c.setCellValue(d);
			} catch (NumberFormatException e) {
				c.setCellValue(s);
			}

		}
	}

	/**
	 * Creates the row from.
	 *
	 * @param o the o
	 * @return the row
	 */
	private Row createRowFrom(IRecordable o) {
		Integer lastRow;
		Row r;
		String[] data;

		if (dicoLocations.containsKey(o.getClassement()))
			lastRow = dicoLocations.get(o.getClassement());
		else {
			lastRow = 0;
			Sheet logSheet = wb.createSheet(o.getClassement());
			r = logSheet.createRow(0);
			data = o.getTitles();
			fillRowWith(r, data);
			dicoLocations.put(o.getClassement(), lastRow);
		}

		Sheet sh = wb.getSheet(o.getClassement());
		r = sh.createRow(lastRow + 1);

		// r = lastRow.getSheet().createRow(lastRow.getRowNum() + 1);
		data = o.getRecords();
		fillRowWith(r, data);
		dicoLocations.replace(o.getClassement(), lastRow + 1);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see enstabretagne.base.logger.loggerimpl.AbstractLogger#checkLoggerConf(
	 * enstabretagne.base.logger.loggerimpl.LoggerConf)
	 */
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public String checkLoggerConf(LoggerConf conf) {
		String result = super.checkLoggerConf(conf);
		if (!conf.parametres.containsKey(LoggerParamsNames.DirectoryName))
			result = MessagesLogger.ExcelLoggerDirNotFound + " ParamName(" + LoggerParamsNames.DirectoryName + ")";
		if (!conf.parametres.containsKey(LoggerParamsNames.FileName))
			result = MessagesLogger.ExcelLoggerFileNotFound + " ParamName(" + LoggerParamsNames.FileName + ")";
		if (!conf.parametres.containsKey(LoggerParamsNames.RecordStartTime))
			result = MessagesLogger.ExcelLoggerRecordStartTimeNotFound + " ParamName("
					+ LoggerParamsNames.RecordStartTime + ")";

		return result;

	}

	@Override
	public void save() {
		initsave();
	}

}

/**
* Classe LogicalDateTime.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.time;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import enstabretagne.base.Settings;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.messages.MessagesLogicalTimeDuration;

// TODO: Auto-generated Javadoc
/**
 * The Class LogicalDateTime.
 */
public class LogicalDateTime implements Comparable<LogicalDateTime>{
	
	/** The Constant wellFormedDateSample. */
	public final static String wellFormedDateSample = "01/09/2014 06:03:37.120'";
	
	/** The logical date. */
	LocalDateTime logicalDate;
	
	/** The Constant logicalDateTimeFormatter. */
	public static final DateTimeFormatter logicalDateTimeFormatter;
	
	/** The Constant logicalTimeFormatter. */
	public static final DateTimeFormatter logicalTimeFormatter;
	
	/** The Constant logicalDateFormatter. */
	public static final DateTimeFormatter logicalDateFormatter;
	static {
		logicalTimeFormatter=DateTimeFormatter.ISO_TIME;
		logicalDateFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		DateTimeFormatterBuilder dtfb = new DateTimeFormatterBuilder();
		dtfb.parseCaseInsensitive();
		dtfb.append(logicalDateFormatter);
		dtfb.appendLiteral(Settings.date_time_separator);		
		dtfb.append(logicalTimeFormatter);
		logicalDateTimeFormatter = dtfb.toFormatter();
		
	}
	
	/** The Constant Zero. */
	public static final LogicalDateTime Zero= new LogicalDateTime(Settings.timeOrigin()); 
	
	//Date logique de l'instant reel courant
	public static LogicalDateTime Now() {
		return new LogicalDateTime(LogicalDateTime.logicalDateTimeFormatter.format(LocalDateTime.now()));
	}
	
	/** The Constant MaxValue. */
	public static final LogicalDateTime MaxValue = new LogicalDateTime(LocalDateTime.MAX);
	
	/** The Constant MinValue. */
	public static final LogicalDateTime MinValue = new LogicalDateTime(LocalDateTime.MIN);

	/** The Constant UNDEFINED. */
	public static final LogicalDateTime UNDEFINED = new LogicalDateTime(false);
	
	
	
	/** The is defined. */
	boolean isDefined=true;
	
	/**
	 * Instantiates a new logical date time.
	 *
	 * @param isDefined the is defined
	 */
	private LogicalDateTime(boolean isDefined){
		logicalDate = null;
		isDefined=false;
	}
	
	/**
	 * Logical date from.
	 *
	 * @param dateTimeFrenchFormat the date time french format
	 * @return the logical date time
	 */
	public static LogicalDateTime LogicalDateFrom(String dateTimeFrenchFormat) {
		try {
			return new LogicalDateTime(LocalDateTime.parse(dateTimeFrenchFormat,logicalDateTimeFormatter));
		}
		catch(DateTimeParseException e) {
			
			System.err.println(e.getMessage());
			System.err.println("Exemple de date bien form�e : "+ wellFormedDateSample);
			return null;
		}
	}
	
	/**
	 * Instantiates a new logical date time.
	 *
	 * @param dateTimeFrenchFormat the date time french format (ISO = JJ/MM/AAAA HH:MM:SS.SSSSSSS)
	 */
	public LogicalDateTime(String dateTimeFrenchFormat) {
		logicalDate =LocalDateTime.parse(dateTimeFrenchFormat,logicalDateTimeFormatter);
	}
	
	/**
	 * Instantiates a new logical date time.
	 *
	 * @param ldt the ldt
	 */
	private LogicalDateTime(LocalDateTime ldt){
		logicalDate = ldt;
	}
	
	/**
	 * Soustract.
	 *
	 * @param d the d
	 * @return the logical duration
	 */
	public LogicalDuration soustract(LogicalDateTime d){
		return LogicalDuration.soustract(this, d);
	}
	
	/**
	 * Replace by.
	 *
	 * @param d the d
	 */
	public void replaceBy(LogicalDateTime d){
		logicalDate = d.logicalDate;
	}

	/**
	 * Adds the.
	 *
	 * @param offset the offset
	 * @return the logical date time
	 */
	public LogicalDateTime add(LogicalDuration offset) {
		if(!isDefined)
			Logger.Fatal(this, "add", MessagesLogicalTimeDuration.LogicalDateIsNotDefined);
		if(offset == LogicalDuration.POSITIVE_INFINITY)
			return LogicalDateTime.UNDEFINED;
		return new LogicalDateTime(logicalDate.plus(offset.logicalDuration));
	}
	
	/**
	 * Adds the.
	 *
	 * @param date the date
	 * @param dt the dt
	 * @return the logical date time
	 */
	public static LogicalDateTime add(LogicalDateTime date,LogicalDuration dt) {
		if(!date.isDefined)
			Logger.Fatal(null, "add", MessagesLogicalTimeDuration.LogicalDateIsNotDefined);

		return new LogicalDateTime(date.logicalDate.plus(dt.logicalDuration));
	}
	
	
	/**
	 * Truncate to years.
	 *
	 * @return the logical date time
	 */
	public LogicalDateTime truncateToYears() {
		return new LogicalDateTime(logicalDate.truncatedTo(ChronoUnit.YEARS));
	}

	/**
	 * Truncate to days.
	 *
	 * @return the logical date time
	 */
	public LogicalDateTime truncateToDays() {
		return new LogicalDateTime(logicalDate.truncatedTo(ChronoUnit.DAYS));
	}
	
	/**
	 * Truncate to hours.
	 *
	 * @return the logical date time
	 */
	public LogicalDateTime truncateToHours() {
		return new LogicalDateTime(logicalDate.truncatedTo(ChronoUnit.HOURS));
	}

	/**
	 * Truncate to minutes.
	 *
	 * @return the logical date time
	 */
	public LogicalDateTime truncateToMinutes() {
		return new LogicalDateTime(logicalDate.truncatedTo(ChronoUnit.MINUTES));
	}
	
	/**
	 * Gets the day of week.
	 *
	 * @return the day of week
	 */
	public DayOfWeek getDayOfWeek() {
		return logicalDate.getDayOfWeek();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(LogicalDateTime o) {		
		return logicalDate.compareTo(o.logicalDate);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return logicalDateTimeFormatter.format(logicalDate);
	}

	/**
	 * Gets the copy.
	 *
	 * @return the copy
	 */
	public LogicalDateTime getCopy(){
		return new LogicalDateTime(logicalDate);
	}

	public static boolean EstBienStructuree(String dateDemandee) {
		try {
		LocalDateTime.parse(dateDemandee,logicalDateTimeFormatter);
		return true;
		}
		catch(Exception e)
		{
			return false;
			
		}
	}
}


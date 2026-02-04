/**
* Classe LogicalDuration.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.time;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import enstabretagne.base.Settings;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.messages.MessagesLogicalTimeDuration;

// TODO: Auto-generated Javadoc
/**
 * The Class LogicalDuration.
 */
public class LogicalDuration implements Comparable<LogicalDuration> {

	/** The Constant f. */
	public static final DecimalFormat f = new DecimalFormat();
	static {
		f.setGroupingSize(0);
		f.setMaximumFractionDigits(9);
	}
	
	/** The Constant MAX_VALUE. */
	//807 voir comment faire mieux l'arrondi en double de LongMaxValue. En retirant 807 ceci permet de faire un arrondi double inferieur
	public static final LogicalDuration MAX_VALUE = LogicalDuration.ofSeconds(Long.MAX_VALUE-807);
	
	/** The Constant POSITIVE_INFINITY. */
	public static final LogicalDuration POSITIVE_INFINITY = new LogicalDuration((Duration)null);
	
	/** The Constant ZERO. */
	public static final LogicalDuration ZERO = new LogicalDuration(Duration.ZERO);
	
	/** The Constant TickValue. */
	public static final LogicalDuration TickValue = LogicalDuration.ofSeconds(Settings.TickValue());
	
	/** The logical duration. */
	Duration logicalDuration;
	

	/**
	 * Gets the total of hours.
	 *
	 * @return the total of hours
	 */
	public long getTotalOfHours(){
		return logicalDuration.toHours();
	}

	/**
	 * Gets the total of minutes.
	 *
	 * @return the total of minutes
	 */
	public long getTotalOfMinutes(){
		return logicalDuration.toMinutes();
	}

	/**
	 * Gets the total of seconds.
	 *
	 * @return the total of seconds
	 */
	public double getTotalOfSeconds(){
		return Math.round(logicalDuration.toNanos()/1000000000);
	}
	
	/**
	 * Max.
	 *
	 * @param d1 the d 1
	 * @param d2 the d 2
	 * @return the logical duration
	 */
	public static LogicalDuration Max(LogicalDuration d1,LogicalDuration d2)
	{
		if(d1.logicalDuration==null)//infini
			return d1;
		if(d2.logicalDuration==null)//infini
			return d2;
		
		if(d1.logicalDuration.compareTo(d2.logicalDuration)>=0)
			return d1;
		else
			return d2;
	}

	/**
	 * Soustract.
	 *
	 * @param d1 the d 1
	 * @param d2 the d 2
	 * @return the logical duration
	 */
	public static LogicalDuration soustract(LogicalDateTime d1,LogicalDateTime d2){
		if(!d1.isDefined)
			Logger.Fatal(d1, "add", MessagesLogicalTimeDuration.LogicalDateIsNotDefined);
		if(!d2.isDefined)
			Logger.Fatal(d2, "add", MessagesLogicalTimeDuration.LogicalDateIsNotDefined);

		return new LogicalDuration(Duration.between(d2.logicalDate,d1.logicalDate));
	}
	
	/**
	 * Instantiates a new logical duration.
	 *
	 * @param d the d
	 */
	private LogicalDuration(Duration d) {
		logicalDuration = d;
	}
	
	/**
	 * Of day.
	 *
	 * @param days the days
	 * @return the logical duration
	 */
	public static LogicalDuration ofDay(long days){
		return new LogicalDuration(Duration.ofDays(days));
	}

	/**
	 * Of hours.
	 *
	 * @param hours the hours
	 * @return the logical duration
	 */
	public static LogicalDuration ofHours(long hours){
		return new LogicalDuration(Duration.ofHours(hours));
	}

	/**
	 * Of millis.
	 *
	 * @param millis the millis
	 * @return the logical duration
	 */
	public static LogicalDuration ofMillis(long millis){
		return new LogicalDuration(Duration.ofMillis(millis));
	}
	
	/**
	 * Of minutes.
	 *
	 * @param minutes the minutes
	 * @return the logical duration
	 */
	public static LogicalDuration ofMinutes(long minutes){
		return new LogicalDuration(Duration.ofMinutes(minutes));
	}

	/**
	 * Of nanos.
	 *
	 * @param nanos the nanos
	 * @return the logical duration
	 */
	public static LogicalDuration ofNanos(long nanos){
		return new LogicalDuration(Duration.ofNanos(nanos));
	}

	/**
	 * Of seconds.
	 *
	 * @param seconds the seconds
	 * @return the logical duration
	 */
	public static LogicalDuration ofSeconds(double seconds){

		if(seconds>Long.MAX_VALUE) {
			Logger.Fatal(null, "ofSeconds", MessagesLogicalTimeDuration.DoubleValueTooHigh, Long.MAX_VALUE);
			System.exit(1);
		}
//		return new LogicalDuration(Duration.parse("PT"+f.format(seconds)+"s"));
		long nanos = Math.round(seconds *1000000000);
		return new LogicalDuration(Duration.ofNanos(nanos));
	}

	/**
	 * Double value.
	 *
	 * @return the double
	 */
	public double DoubleValue() {
		
		return logicalDuration.toNanos()/1000000000.0;
	}

	
	/**
	 * Quantify.
	 *
	 * @param dt the dt
	 * @return the logical duration
	 */
	public static LogicalDuration Quantify(LogicalDuration dt) {
	      return TickValue.mult(Math.round(dt.DoubleValue() / TickValue.DoubleValue()));
	}


	/**
	 * Mult.
	 *
	 * @param round the round
	 * @return the logical duration
	 */
	public LogicalDuration mult(long round) {
		if(logicalDuration==null) return LogicalDuration.POSITIVE_INFINITY;
		return new LogicalDuration(logicalDuration.multipliedBy(round));
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(LogicalDuration o) {
		
		if(logicalDuration==null&&o.logicalDuration==null)
			return 0;
		if(logicalDuration==null)
			return 1;
		if(o.logicalDuration==null)
			return -1;
		return logicalDuration.compareTo(o.logicalDuration);
	}

	/**
	 * Adds the.
	 *
	 * @param value the value
	 * @return the logical duration
	 */
	public LogicalDuration add(LogicalDuration value) {
		if(logicalDuration==null || value.logicalDuration==null)
			return LogicalDuration.POSITIVE_INFINITY;
		return new LogicalDuration(logicalDuration.plus(value.logicalDuration));
	}

	/**
	 * From string.
	 *
	 * @param dureeAsISO_Time the duree as IS O time
	 * @return the logical duration
	 */
	public static LogicalDuration fromString(String dureeAsISO_Time) {
		LocalTime lt=LocalTime.parse(dureeAsISO_Time,DateTimeFormatter.ISO_TIME);
		return LogicalDuration.ofSeconds(lt.getHour()*3600+lt.getMinute()*60+lt.getSecond()+((double) lt.getNano())/1000000000);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		if(this==POSITIVE_INFINITY)
			return "Infini";
		
		long years = Math.round(logicalDuration.toDays()/365) ;
		long days = logicalDuration.toDays()% 365;
		long hours = logicalDuration.toHours()%24;
		long minutes = logicalDuration.toMinutes()%60;
		long seconds = Math.round(logicalDuration.toNanos()/1000000000) % 60;
		
		double sec = logicalDuration.toNanos()/1000000000.0 - Math.round(logicalDuration.toNanos()/1000000000)+ seconds;
		String s = "";
		
		if(years !=0){
			s= years +"a"+days+"j"+hours+"h"+minutes+"m"+sec+"s";
		}
		else if(days !=0){
			s = days+"j"+hours+"h"+minutes+"m"+sec+"s";
		}
		else if(hours !=0){
			s=hours+"h"+minutes+"m"+sec+"s";
		}
		else if(minutes !=0){
			s=minutes+"m"+sec+"s";
		}
		else
			s = sec+"s";
			
		
		return s;
	}
}


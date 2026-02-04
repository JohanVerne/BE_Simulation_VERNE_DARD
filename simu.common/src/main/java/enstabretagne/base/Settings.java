/**
* Classe Settings.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
// TODO: Auto-generated Javadoc
/**
 * The Class Settings.
 */
public class Settings {
	
	/** The Constant jsonb. */
	private static final Jsonb jsonb;
	static {
		JsonbConfig config = new JsonbConfig().withFormatting(true);
		jsonb= JsonbBuilder.create(config);
	}
	
	public static Object fromJson(String content,Class<?> c) {
		return getJsonb().fromJson(content, c);
	}

	/** The Constant configuration_dir. */
	public static final String configuration_dir="conf";
	
	/** The Constant controleurSuffixe. */
	public static final String controleurSuffixe = "_controler";
	
	/** The Constant date_time_separator. */
	public static final String date_time_separator = " ";

	public static final String resultDir="resultats";
	public static final String journalGlobal="journalGlobalExecution";
	public static final String defaultLogName="JournalisationParDefaut";
	public static final boolean displayLog=true;

	/**
	 * Sep.
	 *
	 * @return the char
	 */
	public static char sep(){
		return ';';
	}
	
	/**
	 * Use portable random generator.
	 *
	 * @return true, if successful
	 */
	public static boolean UsePortableRandomGenerator() {
		return false;
	}

	/**
	 * Use one random generator per agent.
	 *
	 * @return true, if successful
	 */
	public static boolean UseOneRandomGeneratorPerAgent() {
		return false;
	}

	
	/**
	 * Use binary tree for event list.
	 *
	 * @return true, if successful
	 */
	public static boolean UseBinaryTreeForEventList() {
		return false;
	}

	/**
	 * Checks if is engine integrity checked.
	 *
	 * @return true, if successful
	 */
	public static boolean IsEngineIntegrityChecked() {
		return false;
	}

	/**
	 * Default synchro order.
	 *
	 * @return the int
	 */
	public static int DefaultSynchroOrder() {
		return 4;
	}
	
	/**
	 * Tick value.
	 *
	 * @return the double
	 */
	public static double TickValue(){
		return 0.0001;
	}
	
	/**
	 * Time origin.
	 *
	 * @return the local date time
	 */
	public static LocalDateTime timeOrigin(){
		return LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
	}
	
	/**
	 * Filter engine logs.
	 *
	 * @return true, if successful
	 */
	public static boolean filterEngineLogs() {
		return true;
	}

	public static Jsonb getJsonb() {
		return jsonb;
	}
}


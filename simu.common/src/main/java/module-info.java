/**
 * 
 */
/**
 * @author Aude et Olivier
 *
 */
module simu_base_common {
	requires transitive jakarta.json.bind;
	requires transitive jakarta.json;
	requires transitive org.eclipse.yasson;

	requires transitive java.sql;
	requires org.apache.poi.poi;
	requires org.apache.poi.ooxml;
	
	exports enstabretagne.base.logger;
	exports enstabretagne.base.time;
	exports enstabretagne.base;
	exports enstabretagne.base.math;
	exports enstabretagne.simulation.basics;

}
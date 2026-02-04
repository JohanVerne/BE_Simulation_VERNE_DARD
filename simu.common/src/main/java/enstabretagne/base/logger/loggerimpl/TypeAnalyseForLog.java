/*
 * 
 */
package enstabretagne.base.logger.loggerimpl;

import java.lang.reflect.Method;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeAnalyseForLog.
 */
public class TypeAnalyseForLog {
	
	/** The c. */
	Class<?> c;
	
	/** The classement. */
	String classement;
	
	/** The m L. */
	HashMap<String,Method> mL;
	
	/** The titles. */
	String[] titles;

	/**
	 * Instantiates a new type analyse for log.
	 *
	 * @param class1 the class 1
	 * @param name the name
	 * @param mL2 the m L 2
	 */
	public TypeAnalyseForLog(Class<? extends Object> class1, String name, HashMap<String, Method> mL2) {
		c = class1;
		classement = name;
		if(mL2!=null) {			
			titles = mL2.keySet().toArray(new String[0]);
			mL=mL2;
		}
		else
		{
			titles =new String[0];
			mL = new HashMap<String, Method>();
		}
	}
	
	/**
	 * Gets the titles.
	 *
	 * @return the titles
	 */
	public String[] getTitles() {
		return titles;
	}

	/**
	 * Gets the classement.
	 *
	 * @return the classement
	 */
	public String getClassement() {
		return classement;
	}
	
}

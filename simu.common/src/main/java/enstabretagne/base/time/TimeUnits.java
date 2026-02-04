/**
* Classe TimeUnits.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.time;

// TODO: Auto-generated Javadoc
/**
 * The Enum TimeUnits.
 */
public enum TimeUnits {
	
	/** The Milliseconde. */
	Milliseconde("Milliseconde"),
	
	/** The Seconde. */
	Seconde("Seconde"),
	
	/** The Minute. */
	Minute("Minute"),
	
	/** The Heure. */
	Heure("Heure"),
	
	/** The Annee. */
	Annee("Annee");
	
	/** The name. */
	private final String name;
	
	/**
	 * Instantiates a new time units.
	 *
	 * @param name the name
	 */
	private TimeUnits(String name){
		this.name=name;
	}
		
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}


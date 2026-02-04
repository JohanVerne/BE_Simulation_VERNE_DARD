/**
* Classe IListenerList.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.logger;

// TODO: Auto-generated Javadoc
/**
 * The Interface IListenerList.
 *
 * @param <C> the generic type
 */
public interface IListenerList<C> {
	
	/**
	 * Adds the.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	boolean add(C o);
	
	/**
	 * Removes the.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	boolean remove(C o);
}


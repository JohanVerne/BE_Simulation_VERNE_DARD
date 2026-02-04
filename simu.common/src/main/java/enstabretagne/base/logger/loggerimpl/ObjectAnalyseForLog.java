/*
 * 
 */
package enstabretagne.base.logger.loggerimpl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import enstabretagne.base.logger.IRecordable;
import enstabretagne.base.messages.MessagesLogger;

// TODO: Auto-generated Javadoc
/**
 * The Class ObjectAnalyseForLog.
 */
public class ObjectAnalyseForLog implements IRecordable {
	
	/** The o. */
	Object o;
	
	/** The to. */
	TypeAnalyseForLog to;
	
	/** The records. */
	String[] records;
	
	/**
	 * Gets the object type.
	 *
	 * @return the object type
	 */
	public Class<?> getObjectType()
	{
		return o.getClass();
	}
	
	
	/**
	 * Instantiates a new object analyse for log.
	 *
	 * @param to the to
	 * @param o the o
	 */
	public ObjectAnalyseForLog(TypeAnalyseForLog to,Object o)
	{
		this.o=o;
				
		
		this.to=to;
		List<String> recordsL = new ArrayList<>();
		for(Method m : to.mL.values())
		{
			try {
				Object res=m.invoke(o, (Object[]) null);
				if(res!=null)
					recordsL.add(res.toString());
				else
					recordsL.add("");
			} catch (Exception e) {
			
				System.err.println(MessagesLogger.LoggerImpossible + "ObjectAnalyseForLog");
				System.err.println("Probleme en executant la methode "+m.getName()+ " sur l'objet "+o.toString()+" de type " +o.getClass().getName());
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
		records = recordsL.toArray(new String[0]);
	}
	
	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.IRecordable#getTitles()
	 */
	@Override
	public String[] getTitles() {
		return to.getTitles();
	}

	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.IRecordable#getRecords()
	 */
	@Override
	public String[] getRecords() {
		return records;
	}

	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.IRecordable#getClassement()
	 */
	@Override
	public String getClassement() {
		return to.getClassement();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return o.toString();
	}

}

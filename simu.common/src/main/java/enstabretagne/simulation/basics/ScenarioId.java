/*basic
 * 
 */
package enstabretagne.simulation.basics;

// TODO: Auto-generated Javadoc
/**
 * The Class ScenarioId.
 */
public class ScenarioId {
	
	/** The scenario id. */
	private String scenarioId;
	
	/** The replique number. */
	private long repliqueNumber;

	/** The seed */
	private double seed;

	/**
	 * Instantiates a new scenario id.
	 *
	 * @param scenarioId the scenario id
	 */
	public ScenarioId(String scenarioId) {
		this(scenarioId,0);
	}

	/**
	 * Instantiates a new scenario id.
	 *
	 * @param scenarioId the scenario id
	 * @param repliqueNumber the replique number
	 */
	public ScenarioId(String scenarioId, long repliqueNumber) {
		super();
		this.scenarioId = scenarioId;
		this.repliqueNumber = repliqueNumber;
	}

	/**
	 * Instantiates a new scenario id.
	 *
	 * @param scenarioId the scenario id
	 * @param repliqueNumber the replique number
	 */
	public ScenarioId(String scenarioId, long repliqueNumber,double seed) {
		super();
		this.scenarioId = scenarioId;
		this.repliqueNumber = repliqueNumber;
		this.seed = seed;
	}
	/**
	 * Gets the scenario id.
	 *
	 * @return the scenario id
	 */
	public String getScenarioId() {
		return scenarioId + " replique=" + repliqueNumber + " seed=" + seed;
	}
	
	/**
	 * Sets the scenario id.
	 *
	 * @param scenarioId the new scenario id
	 */
	public void setScenarioId(String scenarioId) {
		this.scenarioId = scenarioId;
	}
	
	/**
	 * Gets the replique number.
	 *
	 * @return the replique number
	 */
	public long getRepliqueNumber() {
		return repliqueNumber;
	}
	
	/**
	 * Sets the replique number.
	 *
	 * @param repliqueNumber the new replique number
	 */
	public void setRepliqueNumber(long repliqueNumber) {
		this.repliqueNumber = repliqueNumber;
	}
	
	/** The Constant ScenarioID_NULL. */
	public final static ScenarioId ScenarioID_NULL = new ScenarioId("DefaultScenario",0);
	
}

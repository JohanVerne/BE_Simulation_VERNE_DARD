package enstabretagne.engine.simple.framework.api;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.simple.framework.impl.ScenarioInitData;
import enstabretagne.simulation.basics.IScenarioIdProvider;

public interface Scenario {

    /** Called once during engine initialization to create/register entities. */
    void build(SimulationContext context);

    /**
     * Scenario init data (name, start/end dates, seed...).
     * <p>
     * Note: kept for backward compatibility with existing scenarios.
     */
    ScenarioInitData initData();

    /**
     * Inclusive scenario start time.
     * <p>
     * Default implementation reads it from {@link #initData()} when available.
     */
    default LogicalDateTime startTime() {
        var d = initData();
        return d == null ? null : d.getDebut();
    }

    /**
     * Inclusive scenario end time.
     * <p>
     * Default implementation reads it from {@link #initData()} when available.
     */
    default LogicalDateTime endTime() {
        var d = initData();
        return d == null ? null : d.getFin();
    }
}

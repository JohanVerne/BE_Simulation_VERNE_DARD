package enstabretagne.engine.simple.framework.api;

import enstabretagne.base.time.LogicalDateTime;

/** Fabrique stateless pour creer des repliques (context) et des runners. */
public interface SimulationSession extends AutoCloseable {
    Scenario Scenario();
    SimulationContext Context();
    SimulationRunner Runner();

    void init();
    void simulate();

}

package enstabretagne.engine.simple.framework.api;

import enstabretagne.base.time.LogicalDateTime;

/** Boucle d'execution d'une replique. */
public interface SimulationRunner {
    void runUntilEmpty(SimulationContext ctx);
    void runTo(SimulationContext ctx, LogicalDateTime end);
    void stop();
}

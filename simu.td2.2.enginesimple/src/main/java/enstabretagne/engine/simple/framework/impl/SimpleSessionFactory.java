package enstabretagne.engine.simple.framework.impl;

import enstabretagne.engine.simple.framework.api.Scenario;
import enstabretagne.engine.simple.framework.api.SimulationSession;
import enstabretagne.engine.simple.framework.api.SimulationSessionFactory;

/**
 * Small local factory to create an enginesimple session.
 *
 * <p>Implements {@link SimulationSessionFactory} so it can be used with {@code SimpleExperimentRunner}.
 */
public final class SimpleSessionFactory implements SimulationSessionFactory {

    @Override
    public SimulationSession newSimulationSession(Scenario scenario) {
        if (scenario == null) throw new IllegalArgumentException("scenario must not be null");

        long seed = 0L;
        if (scenario.initData() != null) {
            // ScenarioInitData stores the seed as a double (graine).
            seed = (long) scenario.initData().getGraine();
        }
        return newSession(scenario, seed);
    }

    /** Backward-compatible helper. */
    public SimulationSession newSession(Scenario scenario, long seed) {
        var ctx = new SimpleContext();
        ctx.random().setSeed(seed);
        return new SimpleSimulationSession(scenario, ctx, new SimpleRunner());
    }
}

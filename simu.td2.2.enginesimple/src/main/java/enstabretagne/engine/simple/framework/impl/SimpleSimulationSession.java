package enstabretagne.engine.simple.framework.impl;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.simple.framework.api.Scenario;
import enstabretagne.engine.simple.framework.api.SimulationContext;
import enstabretagne.engine.simple.framework.api.SimulationRunner;
import enstabretagne.engine.simple.framework.api.SimulationSession;

/** Engine pedagogique : fabrique un contexte (replique) et un runner. */
public final class SimpleSimulationSession implements SimulationSession {

    private final Scenario scenario;
    private final SimulationContext context;
    private final SimulationRunner runner;

    public SimpleSimulationSession(Scenario scenario, SimulationContext context, SimulationRunner runner){
        if (scenario == null) throw new IllegalArgumentException("scenario must not be null");
        if (context == null) throw new IllegalArgumentException("context must not be null");
        if (runner == null) throw new IllegalArgumentException("runner must not be null");
        this.scenario = scenario;
        this.context = context;
        this.runner = runner;
    }

    @Override
    public Scenario Scenario() {
        return scenario;
    }

    @Override
    public SimulationContext Context() {
        return context;
    }

    @Override
    public SimulationRunner Runner() {
        return runner;
    }

    @Override
    public void init() {
        LogicalDateTime start = scenario.startTime();
        LogicalDateTime end   = scenario.endTime();

        // Harden invariants: (start, end) are optional but must be coherent if both are present.
        if (start != null && end != null && start.compareTo(end) > 0) {
            throw new IllegalArgumentException("Scenario time window is invalid: startTime > endTime");
        }

        // Enforce that the simulation clock starts at scenario start (if provided).
        if (start != null) {
            context.clock().set(start);
        }

        scenario.build(Context());
        for (var e : Context().registry().entities()) {
            e.init();
        }
    }

    @Override
    public void simulate() {
        LogicalDateTime start = scenario.startTime();
        LogicalDateTime end   = scenario.endTime();

        // If someone changed the clock after init, still guarantee we never run "before start".
        if (start != null && context.clock().now().compareTo(start) < 0) {
            context.clock().set(start);
        }

        if (end != null) {
            // runTo is inclusive: tasks strictly after end are ignored by the runner.
            runner.runTo(context, end);
        } else {
            runner.runUntilEmpty(context);
        }
    }

    @Override
    public void close() throws Exception {

        try { runner.stop(); } catch (Exception ignored) {}
        try { context.close(); } catch (Exception ignored) {}

        // Keep the Logger usable even after a session ends.
        // (If another session starts, SimuEngine.newSession will set a new provider.)
        Logger.setDateProvider(null);
    }
}

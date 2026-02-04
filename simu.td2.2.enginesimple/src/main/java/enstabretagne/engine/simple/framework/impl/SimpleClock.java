package enstabretagne.engine.simple.framework.impl;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.simple.framework.api.SimulationClock;

/** Implementation simple de {@link SimulationClock}. */
public final class SimpleClock implements SimulationClock {

    private LogicalDateTime now;

    public SimpleClock() {
        now = LogicalDateTime.Zero;
    }
    @Override
    public LogicalDateTime now() {
        return now;
    }

    @Override
    public void set(LogicalDateTime t) {
        this.now = t;
    }

    @Override
    public void reset() {
        this.now = LogicalDateTime.Zero;
    }
}

package enstabretagne.engine.simple.framework.impl;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.simple.framework.api.SimulationRunner;
import enstabretagne.engine.simple.framework.api.SimulationContext;

/** Runner minimal : execute toutes les taches jusqu'a ce que le scheduler soit vide. */
public final class SimpleRunner implements SimulationRunner {

    @Override
    public void runUntilEmpty(SimulationContext ctx) {
        runTo(ctx,null);
    }

    @Override
    public void runTo(SimulationContext ctx, LogicalDateTime end) {
        if (ctx == null) throw new IllegalArgumentException("ctx must not be null");

        while (ctx.scheduler().hasNext()) {
            LogicalDateTime t = ctx.scheduler().nextTime();
            if (end!=null && t.compareTo(end) > 0)
            {
                Logger.Information(this, "runto", "End Time reached");
                return;
            }
            ctx.clock().set(t);
            Runnable task = ctx.scheduler().poll();
            task.run();
        }
    }

    @Override
    public void stop() {

    }
}

package enstabretagne.engine.simple.framework.api;

import enstabretagne.base.time.LogicalDateTime;

/** Planificateur minimal : ordonne des taches dans le temps simule. */
public interface Scheduler {
    void scheduleAt(LogicalDateTime time, Runnable task);

    boolean hasNext();
    LogicalDateTime nextTime();
    Runnable poll();

    void clear();
}

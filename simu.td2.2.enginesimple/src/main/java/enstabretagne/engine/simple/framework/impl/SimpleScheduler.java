package enstabretagne.engine.simple.framework.impl;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.simple.framework.api.Scheduler;

import java.util.PriorityQueue;

/**
 * Scheduler minimal base sur une PriorityQueue.
 * - ordre par temps croissant
 * - ordre FIFO a temps egal via un numero de sequence
 */
public final class SimpleScheduler implements Scheduler {

    private static final class SimTimeEvent implements Comparable<SimTimeEvent> {
        final LogicalDateTime time;
        final long seq;
        final Runnable task;

        SimTimeEvent(LogicalDateTime time, long seq, Runnable task) {
            this.time = time;
            this.seq = seq;
            this.task = task;
        }

        @Override
        public int compareTo(SimTimeEvent o) {
            int c = this.time.compareTo(o.time);
            if (c != 0) return c;
            return Long.compare(this.seq, o.seq);
        }
    }

    private final PriorityQueue<SimTimeEvent> q = new PriorityQueue<>();
    private long seq = 0;

    @Override
    public void scheduleAt(LogicalDateTime time, Runnable task) {
        if (task == null) throw new IllegalArgumentException("task must not be null");
        q.add(new SimTimeEvent(time, seq++, task));
    }



    @Override
    public boolean hasNext() {
        return !q.isEmpty();
    }

    @Override
    public LogicalDateTime nextTime() {
        if (q.isEmpty()) throw new IllegalStateException("no scheduled task");
        return q.peek().time;
    }

    @Override
    public Runnable poll() {
        if (q.isEmpty()) throw new IllegalStateException("no scheduled task");
        return q.poll().task;
    }

    @Override
    public void clear() {
        q.clear();
        seq = 0;
    }
}

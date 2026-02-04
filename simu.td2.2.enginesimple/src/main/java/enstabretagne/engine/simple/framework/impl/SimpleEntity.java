package enstabretagne.engine.simple.framework.impl;

import enstabretagne.base.math.MoreRandom;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.simple.framework.api.SimpleCapability;
import enstabretagne.engine.simple.framework.api.SimpleEntityServices;
import enstabretagne.engine.simple.framework.api.SimulationContext;

import java.util.*;

/**
 * Entite minimale : composition via capabilities + exposition d'APIs.
 */
public final class SimpleEntity implements SimpleEntityServices {

    private final SimulationContext context;
    private final List<SimpleCapability> caps = new ArrayList<>();
    private final Map<Class<?>, Object> exposed = new HashMap<>();
    private final InitData init;

    SimpleEntity(SimulationContext context,InitData init) {
        this.context = context;this.init = init;
        if(init!=null)
            expose(InitData.class,init);
    }

    public void addCapability(SimpleCapability cap) {
        if (cap == null) throw new IllegalArgumentException("cap must not be null");
        caps.add(cap);
        cap.onAttach(this);
    }

    public void init() {
        for (var c : caps) c.onInit();
    }

    public void terminate() {
        for (var c : caps) c.onTerminate();
        caps.clear();
        exposed.clear();
    }

    @Override
    public <T> void expose(Class<T> apiType, T impl) {
        if (apiType == null) throw new IllegalArgumentException("apiType must not be null");
        if (impl == null) throw new IllegalArgumentException("impl must not be null");
        exposed.put(apiType, impl);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Class<T> apiType) {
        if (apiType == null) throw new IllegalArgumentException("apiType must not be null");
        Object v = exposed.get(apiType);
        return (v == null) ? Optional.empty() : Optional.of((T) v);
    }

    @Override
    public <T> List<T> search(Class<T> apiType) {
        return context.registry().search(apiType);
    }

    @Override
    public void scheduleAt(LogicalDateTime time, Runnable task) {
        context.scheduler().scheduleAt(time, task);
    }

    @Override
    public void scheduleIn(LogicalDuration duration, Runnable task) {
        scheduleAt(now().add(duration),task);
    }

    @Override
    public MoreRandom random() { return context().random();}

    @Override
    public LogicalDateTime now() {
        return context.clock().now();
    }

    public SimulationContext context() {
        return context;
    }
}

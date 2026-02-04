package enstabretagne.engine.simple.framework.api;

import enstabretagne.base.math.MoreRandom;
import enstabretagne.engine.simple.framework.impl.InitData;
import enstabretagne.engine.simple.framework.impl.SimpleEntity;

/**
 * Contexte d'une replique (Option 1) : tout l'etat mutable.
 */
public interface SimulationContext extends AutoCloseable {

    SimulationClock clock();
    Scheduler scheduler();
    EntityRegistry registry();
    MoreRandom random();

    /** Cree et enregistre une nouvelle entite. */
    SimpleEntity createEntity(InitData ini);

    /** Appelle onInit sur toutes les entites existantes. */
    void initAll();

    @Override
    void close();
}

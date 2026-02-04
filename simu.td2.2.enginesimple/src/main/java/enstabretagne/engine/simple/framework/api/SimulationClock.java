package enstabretagne.engine.simple.framework.api;

import enstabretagne.base.time.LogicalDateTime;

/** Horloge de simulation (temps simule courant). */
public interface SimulationClock {

    /** Méthode qui renvoie le temps logique */
    LogicalDateTime now();
    void set(LogicalDateTime t);
    void reset();

}

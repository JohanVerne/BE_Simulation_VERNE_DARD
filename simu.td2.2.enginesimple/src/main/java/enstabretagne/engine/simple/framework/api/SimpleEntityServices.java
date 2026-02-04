package enstabretagne.engine.simple.framework.api;

import enstabretagne.base.math.MoreRandom;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;

import java.util.List;
import java.util.Optional;

/**
 * Services minimaux exposes a une capability ou une action.
 *
 * Objectif pedagogique :
 * - exposer/consommer des APIs (interfaces) sans dependances sur les classes concretes
 * - rechercher des APIs dans une registry simple
 * - planifier du code dans le temps simule
 */
public interface SimpleEntityServices {

    void init();

    /** Expose une API (interface) vers une implementation concrete. */
    <T> void expose(Class<T> apiType, T impl);

    /** Recuperation optionnelle d'une API exposee par l'entite. */
    <T> Optional<T> get(Class<T> apiType);

    /** Recherche d'APIs exposees dans la registry de la simulation. */
    <T> List<T> search(Class<T> apiType);

    /** Planifie une tache au temps simule . */
    void scheduleAt(LogicalDateTime time, Runnable task);

    /** Planifie une tache dans une certaine durée . */
    void scheduleIn(LogicalDuration duration, Runnable task);

    /** Fournit un générateur de nombres aléatoires. */
    MoreRandom random();

    /** Temps simule courant. */
    LogicalDateTime now();
}

package enstabretagne.engine.simple.framework.api;

/**
 * Capability minimale.
 *
 * - onAttach: injection des services de l'entite "owner" (abstraction)
 * - onInit: planification d'actions, exposition d'APIs...
 * - onTerminate: nettoyage
 */
public interface SimpleCapability {

    /** Méthode fondamentale qui va permettre à la capacité d'interagir avec le reste du monde via son owner */
    void onAttach(SimpleEntityServices owner);

    /** Méthode déclenchée par son owner et de laquelle se produira l'effet "boule de neige" vu en cours */
    default void onInit() {}

    default void onTerminate() {}
}

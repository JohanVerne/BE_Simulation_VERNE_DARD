package enstabretagne.engine.simple.framework.api;

import java.util.List;

/**
 * Registry minimale d'entites pour une replique.
 * Objectif pedagogique : pouvoir faire un {@code search(Api.class)} sans dependance sur une entite concrete.
 */
public interface EntityRegistry {

    /** Toutes les entites de la replique (vue non modifiable recommandee). */
    List<SimpleEntityServices> entities();

    /** Recherche de toutes les APIs exposees de type {@code apiType}. */
    <T> List<T> search(Class<T> apiType);

    /** Vide la registry. */
    void clear();
}

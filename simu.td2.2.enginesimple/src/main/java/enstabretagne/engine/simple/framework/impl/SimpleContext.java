package enstabretagne.engine.simple.framework.impl;

import enstabretagne.base.math.MoreRandom;
import enstabretagne.engine.simple.framework.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contexte de replique (Option 1) : tout l'etat mutable d'une simulation simple.
 *
 * Pedagogie :
 * - une registry tres simple : la liste des entites de la replique
 * - un service de recherche par type : {@link EntityRegistry#search(Class)}
 */
public final class SimpleContext implements SimulationContext {

    private final SimpleClock clock = new SimpleClock();
    private final SimpleScheduler scheduler = new SimpleScheduler();
    private final SimpleRegistry registry = new SimpleRegistry(this);
    private final MoreRandom random = new MoreRandom();


    @Override
    public SimulationClock clock() {
        return clock;
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public EntityRegistry registry() {
        return registry;
    }

    @Override
    public MoreRandom random() {return random;}

    /** Cree une entite et l'enregistre dans la registry simple. */
    @Override
    public SimpleEntity createEntity(InitData ini) {
        return registry.createEntity(ini);
    }

    /** Initialise toutes les entites (appel {@code onInit()} des capabilities). */
    @Override
    public void initAll() {
        for (var e : registry.entitiesInternal()) e.init();
    }

    @Override
    public void close() {
        // Terminer les entites d'abord (elles pourraient vouloir logguer/terminer proprement).
        for (SimpleEntity e : registry.entitiesInternal()) {
            e.terminate();
        }
        registry.clear();

        // Puis nettoyer les structures temporelles.
        scheduler.clear();
        clock.reset();
    }

    /**
     * Implementation de registry minimale.
     * Note : pour garder l'exemple simple, on expose la liste d'entites en lecture seule.
     */
    private static final class SimpleRegistry implements EntityRegistry {
        private final SimpleContext ctx;
        private final List<SimpleEntity> entities = new ArrayList<>();

        SimpleRegistry(SimpleContext ctx) {
            this.ctx = ctx;
        }

        SimpleEntity createEntity(InitData ini) {
            var e = new SimpleEntity(ctx,ini);
            entities.add(e);
            return e;
        }

        List<SimpleEntity> entitiesInternal() {
            return entities;
        }

        @Override
        public List<SimpleEntityServices> entities() {
            return Collections.unmodifiableList(entities);
        }

        @Override
        public <T> List<T> search(Class<T> apiType) {
            if (apiType == null) throw new IllegalArgumentException("apiType must not be null");
            List<T> out = new ArrayList<>();
            for (var e : entities) {
                e.get(apiType).ifPresent(out::add);
            }
            return out;
        }

        @Override
        public void clear() {
            entities.clear();
        }
    }
}

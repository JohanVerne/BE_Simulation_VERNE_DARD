package enstabretagne.be.salon;

import enstabretagne.engine.simple.framework.api.ExperimentPlan;
import enstabretagne.engine.simple.framework.api.Scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Plan d'expérience pour le salon de coiffure.
 * Permet de définir une série de scénarios à exécuter par répliques.
 */
public class SalonExperimentPlan implements ExperimentPlan {

    private final List<Scenario> scenarios = new ArrayList<>();
    private final long maxReplicaNumber;
    private int currentIndex = -1;

    public SalonExperimentPlan(long maxReplicaNumber) {
        this.maxReplicaNumber = maxReplicaNumber;
    }

    /**
     * Ajoute un scénario au plan d'expérience.
     */
    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    @Override
    public long maxReplicaNumber() {
        return maxReplicaNumber;
    }

    @Override
    public Scenario currentScenario() {
        if (currentIndex >= 0 && currentIndex < scenarios.size()) {
            return scenarios.get(currentIndex);
        }
        return null;
    }

    @Override
    public boolean hasNextScenario() {
        return currentIndex + 1 < scenarios.size();
    }

    @Override
    public Scenario nextScenario() {
        currentIndex++;
        return scenarios.get(currentIndex);
    }
}

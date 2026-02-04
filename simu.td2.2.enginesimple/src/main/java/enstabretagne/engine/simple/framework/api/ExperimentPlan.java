package enstabretagne.engine.simple.framework.api;

public interface ExperimentPlan {
    long maxReplicaNumber();
    Scenario currentScenario();
    boolean hasNextScenario();
    Scenario nextScenario();
}

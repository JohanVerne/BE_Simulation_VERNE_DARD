package enstabretagne.engine.simple.framework.api;

public interface ExperimentPlanSession {
    ExperimentPlan plan();
    ExperimentRunner runner();
    SimulationSessionFactory simulationSessionFactory();
}

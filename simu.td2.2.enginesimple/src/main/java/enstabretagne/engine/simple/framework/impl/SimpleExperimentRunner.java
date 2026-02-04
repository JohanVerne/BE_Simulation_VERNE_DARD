package enstabretagne.engine.simple.framework.impl;

import enstabretagne.base.logger.Logger;
import enstabretagne.engine.simple.framework.api.ExperimentPlan;
import enstabretagne.engine.simple.framework.api.ExperimentRunner;
import enstabretagne.engine.simple.framework.api.SimulationSessionFactory;
import enstabretagne.simulation.basics.ISimulationDateProvider;

public class SimpleExperimentRunner implements ExperimentRunner {
    @Override
    public void run(SimulationSessionFactory factory, ExperimentPlan plan) {
        Logger.Information(this, "main", "Début du plan d'expérience");

        //on boucle sur les sc�narios
        while (plan.hasNextScenario()) {
            var scenario = plan.nextScenario();
            Logger.setScenarioIdProvider(scenario.initData());
            try (var session = factory.newSimulationSession(scenario)) {
                // Make Logger timestamps follow simulation time (global setting)
                ISimulationDateProvider dateProvider = session.Context().clock()::now;
                Logger.setDateProvider(dateProvider);

                session.init();
                session.simulate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Logger.Information(null, "main", "Fin du plan d'expérience");
        Logger.Terminate();
    }
}

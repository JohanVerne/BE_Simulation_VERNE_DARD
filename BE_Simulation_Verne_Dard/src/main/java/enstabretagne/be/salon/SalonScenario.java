package enstabretagne.be.salon;

import enstabretagne.base.logger.Logger;
import enstabretagne.engine.simple.framework.api.Scenario;
import enstabretagne.engine.simple.framework.api.SimulationContext;
import enstabretagne.engine.simple.framework.impl.ScenarioInitData;

/**
 * Scénario du salon de coiffure.
 * Construit les entités (salon, coiffeurs, générateur de clients)
 * et les enregistre dans le contexte de simulation.
 */
public class SalonScenario implements Scenario {

    private final SalonInitData initData;

    public SalonScenario(SalonInitData initData) {
        this.initData = initData;
    }

    @Override
    public ScenarioInitData initData() {
        return initData;
    }

    @Override
    public void build(SimulationContext context) {
        Logger.Information(this, "build", "Construction du scénario : %s", initData.getName());

        // Réinitialiser le compteur de clients
        Client.resetCompteur();

        // 1. Créer l'entité Salon (gère la file d'attente et les stats)
        var salonEntity = context.createEntity(null);
        salonEntity.addCapability(new SalonCapability(
                initData.getSeuilSansFavori(),
                initData.getSeuilMemeFavori()
        ));

        // 2. Créer les entités Coiffeurs
        // Pétunia (patronne, toujours présente)
        var petuniaEntity = context.createEntity(null);
        petuniaEntity.addCapability(new CoiffeurCapability(
                CoiffeurType.PETUNIA, true, 0.0,
                initData.getMoyenneCoupe(), initData.getDureePaiementMinutes()
        ));

        // Lumpy (employé, sujet à l'absentéisme)
        var lumpyEntity = context.createEntity(null);
        lumpyEntity.addCapability(new CoiffeurCapability(
                CoiffeurType.LUMPY, false, initData.getProbaAbsence(),
                initData.getMoyenneCoupe(), initData.getDureePaiementMinutes()
        ));

        // Flaky (employé, sujet à l'absentéisme)
        var flakyEntity = context.createEntity(null);
        flakyEntity.addCapability(new CoiffeurCapability(
                CoiffeurType.FLAKY, false, initData.getProbaAbsence(),
                initData.getMoyenneCoupe(), initData.getDureePaiementMinutes()
        ));

        // 3. Créer l'entité Générateur de clients
        var generatorEntity = context.createEntity(null);
        generatorEntity.addCapability(new ClientGeneratorCapability(
                initData.getInterArriveMoyenne(),
                initData.getProbaFavori(),
                initData.getProbaFavoriLumpy()
        ));

        // 4. Créer l'entité de collecte de statistiques
        var statsEntity = context.createEntity(null);
        statsEntity.addCapability(new StatsCapability(initData.getStatsIntervalMinutes()));

        Logger.Information(this, "build",
                "Scénario construit : 1 salon, 3 coiffeurs, 1 générateur de clients, 1 stats.");
    }

    @Override
    public String toString() {
        return "SalonScenario[" + initData.getName() + "]";
    }
}

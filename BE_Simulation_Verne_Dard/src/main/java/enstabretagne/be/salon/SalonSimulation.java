package enstabretagne.be.salon;

import enstabretagne.engine.simple.framework.impl.SimpleExperimentRunner;
import enstabretagne.engine.simple.framework.impl.SimpleSessionFactory;

/**
 * Point d'entrée principal de la simulation du salon de coiffure.
 * 
 * Configure le plan d'expérience avec les paramètres de l'énoncé :
 * 
 *   Salon ouvert de 10h00 à 20h00
 *   3 coiffeurs : Pétunia, Lumpy, Flaky
 *   Client toutes les 12 min en moyenne (Poisson)
 *   Coupe : 19 min en moyenne (exponentielle)
 *   40% des clients ont un favori (3/4 Lumpy, 1/4 Flaky)
 *   Absentéisme : 1/20 par jour par employé
 * 
 */
public class SalonSimulation {

    public static void main(String[] args) {
        System.out.println("=== Simulation du Salon de Coiffure de Pétunia ===");
        System.out.println();

        // --- Configuration du plan d'expérience ---
        int nbRepliques = 10;
        String debut = "01/01/2026 10:00:00";
        String fin   = "01/01/2026 20:00:00";

        SalonExperimentPlan plan = new SalonExperimentPlan(nbRepliques);

        // Scénario 1 : configuration par défaut selon l'énoncé

        for (int r = 1; r <= nbRepliques; r++) {
            SalonInitData initData = new SalonInitData(
                    "Scenario_Base", r, 1000 + r, debut, fin);
            plan.addScenario(new SalonScenario(initData));
        }



        // Scénario 2 : arrivées plus fréquentes (8 min) et coupe plus rapide (15 min)
        /*
        for (int r = 1; r <= nbRepliques; r++) {
            SalonInitData initData = new SalonInitData(
                    "Scenario_Rapide", r, 2000 + r, debut, fin,
                    3,      // nbCoiffeurs
                    8.0,    // interArriveMoyenne (min) — plus fréquent
                    15.0,   // moyenneCoupe (min) — plus rapide
                    0.4,    // probaFavori
                    0.75,   // probaFavoriLumpy
                    0.05,   // probaAbsence
                    6,      // seuilSansFavori
                    3,      // seuilMemeFavori
                    1.0,    // dureePaiementMinutes
                    10      // statsIntervalMinutes
            );
            plan.addScenario(new SalonScenario(initData));
        }
        */

        // Scénario 3 : aucun absentéisme, seuils d'attente plus élevés
        /*
        for (int r = 1; r <= nbRepliques; r++) {
            SalonInitData initData = new SalonInitData(
                    "Scenario_SansAbsence", r, 3000 + r, debut, fin,
                    3,      // nbCoiffeurs
                    12.0,   // interArriveMoyenne
                    19.0,   // moyenneCoupe
                    0.4,    // probaFavori
                    0.75,   // probaFavoriLumpy
                    0.0,    // probaAbsence — aucun absentéisme
                    10,     // seuilSansFavori — seuil relevé
                    5,      // seuilMemeFavori — seuil relevé
                    1.0,    // dureePaiementMinutes
                    10      // statsIntervalMinutes
            );
            plan.addScenario(new SalonScenario(initData));
        }
        */
        // --- Exécution ---
        SimpleSessionFactory factory = new SimpleSessionFactory();
        SimpleExperimentRunner runner = new SimpleExperimentRunner();

        runner.run(factory, plan);

        System.out.println();
        System.out.println("=== Simulation terminée ===");
    }
}

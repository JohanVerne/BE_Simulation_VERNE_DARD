package enstabretagne.be.salon;

import enstabretagne.base.logger.IRecordable;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.logger.ToRecord;
import enstabretagne.engine.simple.framework.api.SimpleCapability;
import enstabretagne.engine.simple.framework.api.SimpleEntityServices;

/**
 * Capacité de collecte de statistiques pour le salon.
 * Enregistre périodiquement la taille de la file d'attente
 * et les indicateurs de performance.
 * Implémente IRecordable pour produire des données exploitables en CSV.
 */
@ToRecord(name = "StatistiquesSalon")
public class StatsCapability implements SimpleCapability, IRecordable {

    private SimpleEntityServices owner;

    /** Données de la dernière mesure */
    private int tailleFile = 0;
    private int nbCoiffeursOccupes = 0;
    private int nbCoiffeursPresents = 0;
    private String heure = "";

    /** Intervalle d'échantillonnage en minutes. */
    private final long intervalleMesureMinutes;

    public StatsCapability(long intervalleMesureMinutes) {
        this.intervalleMesureMinutes = intervalleMesureMinutes;
    }

    @Override
    public void onAttach(SimpleEntityServices owner) {
        this.owner = owner;
    }

    @Override
    public void onInit() {
        // Planifier la première mesure dès le début
        planifierMesure();
    }

    private void planifierMesure() {
        owner.scheduleIn(
                enstabretagne.base.time.LogicalDuration.ofMinutes(intervalleMesureMinutes),
                this::effectuerMesure
        );
    }

    private void effectuerMesure() {
        // Collecter les données
        var salonApis = owner.search(SalonAPI.class);
        var coiffeurApis = owner.search(CoiffeurAPI.class);

        if (!salonApis.isEmpty()) {
            SalonAPI salon = salonApis.get(0);
            tailleFile = salon.getNbClientsEnAttente();
        }

        nbCoiffeursPresents = 0;
        nbCoiffeursOccupes = 0;
        for (CoiffeurAPI c : coiffeurApis) {
            if (c.estPresent()) {
                nbCoiffeursPresents++;
                if (c.estOccupe()) {
                    nbCoiffeursOccupes++;
                }
            }
        }

        heure = owner.now().toString();

        // Enregistrer via le Logger (sera écrit en CSV si configuré)
        Logger.Data(this);

        // Planifier la prochaine mesure
        planifierMesure();
    }

    // ==================== IRecordable ====================

    @Override
    public String[] getTitles() {
        return new String[]{
                "Heure",
                "TailleFileAttente",
                "CoiffeursPresents",
                "CoiffeursOccupes",
                "TauxOccupation"
        };
    }

    @Override
    public String[] getRecords() {
        double tauxOccupation = nbCoiffeursPresents > 0
                ? (double) nbCoiffeursOccupes / nbCoiffeursPresents * 100.0
                : 0.0;
        return new String[]{
                heure,
                String.valueOf(tailleFile),
                String.valueOf(nbCoiffeursPresents),
                String.valueOf(nbCoiffeursOccupes),
                String.format("%.1f", tauxOccupation)
        };
    }

    @Override
    public String getClassement() {
        return "StatistiquesSalon";
    }

    @Override
    public String toString() {
        return "Stats";
    }
}

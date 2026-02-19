package enstabretagne.be.salon;

import enstabretagne.engine.simple.framework.impl.ScenarioInitData;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * Données d'initialisation pour un scénario du salon de coiffure.
 * Étend ScenarioInitData avec les paramètres spécifiques au salon.
 *
 * Tous les paramètres du modèle sont configurables pour faciliter
 * la création de scénarios variés (analyse de sensibilité, etc.).
 */
public class SalonInitData extends ScenarioInitData {

    /** Nombre de coiffeurs dans le salon. */
    private final int nbCoiffeurs;

    /** Durée moyenne inter-arrivée des clients (minutes). */
    private final double interArriveMoyenne;

    /** Durée moyenne d'une coupe (minutes). */
    private final double moyenneCoupe;

    /** Probabilité qu'un client ait un coiffeur favori. */
    private final double probaFavori;

    /** Parmi les clients avec favori, probabilité que le favori soit Lumpy (le reste va à Flaky). */
    private final double probaFavoriLumpy;

    /** Probabilité d'absence par jour pour chaque employé. */
    private final double probaAbsence;

    /** Seuil max de clients en attente pour un client sans favori. */
    private final int seuilSansFavori;

    /** Seuil max de clients en attente avec même favori. */
    private final int seuilMemeFavori;

    /** Durée de paiement en fin de coupe (minutes). */
    private final double dureePaiementMinutes;

    /** Intervalle de mesure des statistiques (minutes). */
    private final long statsIntervalMinutes;

    /**
     * Constructeur complet permettant de paramétrer chaque aspect du scénario.
     * Peut être utilisé directement en Java ou via désérialisation JSON-B.
     */
    @JsonbCreator
    public SalonInitData(
            @JsonbProperty("name") String name,
            @JsonbProperty("replique") int replique,
            @JsonbProperty("graine") double graine,
            @JsonbProperty("debutS") String debutS,
            @JsonbProperty("finS") String finS,
            @JsonbProperty("nbCoiffeurs") int nbCoiffeurs,
            @JsonbProperty("interArriveMoyenne") double interArriveMoyenne,
            @JsonbProperty("moyenneCoupe") double moyenneCoupe,
            @JsonbProperty("probaFavori") double probaFavori,
            @JsonbProperty("probaFavoriLumpy") double probaFavoriLumpy,
            @JsonbProperty("probaAbsence") double probaAbsence,
            @JsonbProperty("seuilSansFavori") int seuilSansFavori,
            @JsonbProperty("seuilMemeFavori") int seuilMemeFavori,
            @JsonbProperty("dureePaiementMinutes") double dureePaiementMinutes,
            @JsonbProperty("statsIntervalMinutes") long statsIntervalMinutes) {
        super(name, replique, graine, debutS, finS);
        this.nbCoiffeurs = nbCoiffeurs;
        this.interArriveMoyenne = interArriveMoyenne;
        this.moyenneCoupe = moyenneCoupe;
        this.probaFavori = probaFavori;
        this.probaFavoriLumpy = probaFavoriLumpy;
        this.probaAbsence = probaAbsence;
        this.seuilSansFavori = seuilSansFavori;
        this.seuilMemeFavori = seuilMemeFavori;
        this.dureePaiementMinutes = dureePaiementMinutes;
        this.statsIntervalMinutes = statsIntervalMinutes;
    }

    /**
     * Constructeur simplifié avec les valeurs par défaut de l'énoncé.
     */
    public SalonInitData(String name, int replique, double graine,
                         String debutS, String finS) {
        this(name, replique, graine, debutS, finS,
                3,      // nbCoiffeurs
                12.0,   // interArriveMoyenne (min)
                19.0,   // moyenneCoupe (min)
                0.4,    // probaFavori
                0.75,   // probaFavoriLumpy (3/4)
                0.05,   // probaAbsence (1/20)
                6,      // seuilSansFavori
                3,      // seuilMemeFavori
                1.0,    // dureePaiementMinutes
                10      // statsIntervalMinutes
        );
    }

    public int getNbCoiffeurs() { return nbCoiffeurs; }
    public double getInterArriveMoyenne() { return interArriveMoyenne; }
    public double getMoyenneCoupe() { return moyenneCoupe; }
    public double getProbaFavori() { return probaFavori; }
    public double getProbaFavoriLumpy() { return probaFavoriLumpy; }
    public double getProbaAbsence() { return probaAbsence; }
    public int getSeuilSansFavori() { return seuilSansFavori; }
    public int getSeuilMemeFavori() { return seuilMemeFavori; }
    public double getDureePaiementMinutes() { return dureePaiementMinutes; }
    public long getStatsIntervalMinutes() { return statsIntervalMinutes; }
}

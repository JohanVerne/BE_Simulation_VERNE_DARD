package enstabretagne.be.salon;

import enstabretagne.base.logger.Logger;
import enstabretagne.engine.simple.framework.api.SimpleCapability;
import enstabretagne.engine.simple.framework.api.SimpleEntityServices;

import java.util.LinkedList;
import java.util.List;

/**
 * Capacité principale du salon de coiffure.
 * Gère la file d'attente unique (FIFO), l'affectation des clients
 * aux coiffeurs selon les règles de compatibilité, et les statistiques.
 */
public class SalonCapability implements SimpleCapability, SalonAPI {

    /** Seuil maximum de clients en attente pour un client sans favori. */
    private final int seuilMaxAttenteSansFavori;

    /** Seuil maximum de clients en attente avec le même favori. */
    private final int seuilMaxAttenteMemeFavori;

    private SimpleEntityServices owner;

    /** File d'attente globale, ordonnée par ordre d'arrivée (FIFO). */
    private final LinkedList<Client> fileAttente = new LinkedList<>();

    // --- Statistiques ---
    private int nbClientsArrivees = 0;
    private int nbClientsServis = 0;
    private int nbClientsPerdus = 0;
    private int nbClientsPerdusAvecFavori = 0;
    private int nbClientsPerdusSansFavori = 0;
    private long tempsAttenteTotal = 0; // en minutes
    private int nbClientsSortis = 0;    // servis (pour calcul temps moyen)
    private long sommeFileAttente = 0;   // pour calculer la longueur moyenne
    private int nbEchantillonsFile = 0;

    /**
     * @param seuilMaxAttenteSansFavori max clients en attente avant qu'un client sans favori parte (6)
     * @param seuilMaxAttenteMemeFavori max clients en attente avec même favori avant départ (3)
     */
    public SalonCapability(int seuilMaxAttenteSansFavori, int seuilMaxAttenteMemeFavori) {
        this.seuilMaxAttenteSansFavori = seuilMaxAttenteSansFavori;
        this.seuilMaxAttenteMemeFavori = seuilMaxAttenteMemeFavori;
    }

    @Override
    public void onAttach(SimpleEntityServices owner) {
        this.owner = owner;
        owner.expose(SalonAPI.class, this);
    }

    @Override
    public void onInit() {
        Logger.Information(this, "onInit", "Salon de coiffure initialisé.");
    }

    @Override
    public void onTerminate() {
        afficherStatistiques();
    }

    // ==================== SalonAPI ====================

    @Override
    public void traiterArriveeClient(Client client) {
        nbClientsArrivees++;

        // Enregistrer taille de file pour statistiques
        enregistrerTailleFile();

        List<CoiffeurAPI> coiffeurs = owner.search(CoiffeurAPI.class);

        if (client.aFavori()) {
            // Vérifier que le favori est présent
            boolean favoriPresent = coiffeurs.stream()
                    .anyMatch(c -> c.getType() == client.getFavori() && c.estPresent());

            if (!favoriPresent) {
                // Le favori est absent : le client part immédiatement
                Logger.Information(this, "traiterArriveeClient",
                        "%s part immédiatement : favori %s absent.",
                        client, client.getFavori().getNom());
                client.setEtat(EtatClient.PARTI);
                clientPerdu(client);
                return;
            }

            // Compter combien de clients en attente ont le même favori
            long nbMemeFavori = fileAttente.stream()
                    .filter(c -> c.aFavori() && c.getFavori() == client.getFavori())
                    .count();

            if (nbMemeFavori >= seuilMaxAttenteMemeFavori) {
                // Trop de clients attendent pour le même favori
                Logger.Information(this, "traiterArriveeClient",
                        "%s part : %d+ clients attendent déjà pour %s.",
                        client, seuilMaxAttenteMemeFavori, client.getFavori().getNom());
                client.setEtat(EtatClient.PARTI);
                clientPerdu(client);
                return;
            }
        } else {
            // Client sans favori : check file totale
            if (fileAttente.size() > seuilMaxAttenteSansFavori) {
                Logger.Information(this, "traiterArriveeClient",
                        "%s part : plus de %d clients en attente.",
                        client, seuilMaxAttenteSansFavori);
                client.setEtat(EtatClient.PARTI);
                clientPerdu(client);
                return;
            }
        }

        // Le client entre dans la file d'attente
        client.setEtat(EtatClient.ATTENTE);
        fileAttente.addLast(client);
        Logger.Information(this, "traiterArriveeClient",
                "%s entre en file d'attente. Taille file : %d",
                client, fileAttente.size());

        // Tenter immédiatement de l'affecter à un coiffeur libre
        tentativeAffectation();
    }

    @Override
    public Client prendreClientCompatible(CoiffeurType coiffeur) {
        for (int i = 0; i < fileAttente.size(); i++) {
            Client c = fileAttente.get(i);
            if (c.estCompatibleAvec(coiffeur)) {
                fileAttente.remove(i);
                return c;
            }
        }
        return null;
    }

    @Override
    public int getNbClientsEnAttente() {
        return fileAttente.size();
    }

    @Override
    public int getNbClientsEnAttenteParFavori(CoiffeurType coiffeur) {
        return (int) fileAttente.stream()
                .filter(c -> c.aFavori() && c.getFavori() == coiffeur)
                .count();
    }

    @Override
    public void clientServi(Client client) {
        nbClientsServis++;
        Logger.Detail(this, "clientServi",
                "%s servi. Total servis : %d", client, nbClientsServis);
    }

    @Override
    public void clientPerdu(Client client) {
        nbClientsPerdus++;
        if (client.aFavori()) {
            nbClientsPerdusAvecFavori++;
        } else {
            nbClientsPerdusSansFavori++;
        }
        Logger.Detail(this, "clientPerdu",
                "%s perdu. Total perdus : %d", client, nbClientsPerdus);
    }

    @Override
    public void notifierCoiffeurLibre(CoiffeurType coiffeur) {
        enregistrerTailleFile();
        tentativeAffectation();
    }

    // ==================== Logique interne ====================

    /**
     * Tente d'affecter des clients en attente aux coiffeurs libres.
     * Parcourt les coiffeurs présents et libres, pour chacun cherche le premier client compatible.
     */
    private void tentativeAffectation() {
        List<CoiffeurAPI> coiffeurs = owner.search(CoiffeurAPI.class);

        for (CoiffeurAPI coiffeur : coiffeurs) {
            if (coiffeur.estPresent() && !coiffeur.estOccupe()) {
                Client client = prendreClientCompatible(coiffeur.getType());
                if (client != null) {
                    // Calculer le temps d'attente du client
                    if (client.getInstantArrivee() != null) {
                        long tempsAttente = Math.abs(
                                client.getInstantArrivee().soustract(owner.now()).getTotalOfMinutes());
                        tempsAttenteTotal += tempsAttente;
                        nbClientsSortis++;
                    }

                    Logger.Information(this, "tentativeAffectation",
                            "%s affecté à %s. File restante : %d",
                            client, coiffeur.getType().getNom(), fileAttente.size());
                    coiffeur.prendreEnCharge(client);
                }
            }
        }
    }

    /** Enregistre la taille courante de la file pour les statistiques. */
    private void enregistrerTailleFile() {
        sommeFileAttente += fileAttente.size();
        nbEchantillonsFile++;
    }

    /** Affiche les statistiques de fin de simulation. */
    private void afficherStatistiques() {
        Logger.Information(this, "stats", "===== STATISTIQUES DU SALON =====");
        Logger.Information(this, "stats", "Clients arrivés           : %d", nbClientsArrivees);
        Logger.Information(this, "stats", "Clients servis            : %d", nbClientsServis);
        Logger.Information(this, "stats", "Clients perdus (total)    : %d", nbClientsPerdus);
        Logger.Information(this, "stats", "  - avec favori           : %d", nbClientsPerdusAvecFavori);
        Logger.Information(this, "stats", "  - sans favori           : %d", nbClientsPerdusSansFavori);

        double tauxServis = nbClientsArrivees > 0 ? (double) nbClientsServis / nbClientsArrivees * 100.0 : 0;
        double tauxPerdus = nbClientsArrivees > 0 ? (double) nbClientsPerdus / nbClientsArrivees * 100.0 : 0;
        Logger.Information(this, "stats", "Taux clients servis       : %.1f%%", tauxServis);
        Logger.Information(this, "stats", "Taux clients perdus       : %.1f%%", tauxPerdus);

        double tempsMoyenAttente = nbClientsSortis > 0 ? (double) tempsAttenteTotal / nbClientsSortis : 0;
        Logger.Information(this, "stats", "Temps moyen attente (min) : %.1f", tempsMoyenAttente);

        double longueurMoyenneFile = nbEchantillonsFile > 0 ?
                (double) sommeFileAttente / nbEchantillonsFile : 0;
        Logger.Information(this, "stats", "Longueur moyenne file     : %.1f", longueurMoyenneFile);
        Logger.Information(this, "stats", "=================================");

        // Export en DataSimple pour CSV
        // Première ligne : titres
        Logger.DataSimple("BilanJournee",
                "NbArrivees", "NbServis", "NbPerdus", "NbPerdusAvecFavori",
                "NbPerdusSansFavori", "TauxServis%", "TauxPerdus%",
                "TempsMoyenAttente_min", "LongueurMoyenneFile");
        // Deuxième ligne : valeurs
        Logger.DataSimple("BilanJournee",
                String.valueOf(nbClientsArrivees),
                String.valueOf(nbClientsServis),
                String.valueOf(nbClientsPerdus),
                String.valueOf(nbClientsPerdusAvecFavori),
                String.valueOf(nbClientsPerdusSansFavori),
                String.format("%.1f", tauxServis),
                String.format("%.1f", tauxPerdus),
                String.format("%.1f", tempsMoyenAttente),
                String.format("%.1f", longueurMoyenneFile));
    }

    // ==================== Getters pour statistiques ====================

    public int getNbClientsArrivees() { return nbClientsArrivees; }
    public int getNbClientsServis() { return nbClientsServis; }
    public int getNbClientsPerdus() { return nbClientsPerdus; }
    public int getNbClientsPerdusAvecFavori() { return nbClientsPerdusAvecFavori; }
    public int getNbClientsPerdusSansFavori() { return nbClientsPerdusSansFavori; }
    public double getTempsMoyenAttente() {
        return nbClientsSortis > 0 ? (double) tempsAttenteTotal / nbClientsSortis : 0;
    }
    public double getLongueurMoyenneFile() {
        return nbEchantillonsFile > 0 ? (double) sommeFileAttente / nbEchantillonsFile : 0;
    }

    @Override
    public String toString() {
        return "Salon";
    }
}

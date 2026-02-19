package enstabretagne.be.salon;

import enstabretagne.base.time.LogicalDateTime;

/**
 * Représente un client du salon de coiffure.
 * Entité dynamique caractérisée par son instant d'arrivée,
 * la possession éventuelle d'un coiffeur favori, et son état courant.
 */
public class Client {

    private static int compteur = 0;

    private final int id;
    private final LogicalDateTime instantArrivee;
    private final boolean aFavori;
    private final CoiffeurType favori; // null si pas de favori
    private EtatClient etat;

    /**
     * Crée un nouveau client.
     *
     * @param instantArrivee instant d'arrivée dans le salon
     * @param aFavori        true si le client a un coiffeur favori
     * @param favori         le coiffeur favori (LUMPY ou FLAKY), null si pas de favori
     */
    public Client(LogicalDateTime instantArrivee, boolean aFavori, CoiffeurType favori) {
        this.id = ++compteur;
        this.instantArrivee = instantArrivee;
        this.aFavori = aFavori;
        this.favori = favori;
        this.etat = EtatClient.ATTENTE;
    }

    /** Réinitialise le compteur d'identifiants (entre scénarios). */
    public static void resetCompteur() {
        compteur = 0;
    }

    public int getId() {
        return id;
    }

    public LogicalDateTime getInstantArrivee() {
        return instantArrivee;
    }

    public boolean aFavori() {
        return aFavori;
    }

    public CoiffeurType getFavori() {
        return favori;
    }

    public EtatClient getEtat() {
        return etat;
    }

    public void setEtat(EtatClient etat) {
        this.etat = etat;
    }

    /**
     * Vérifie si un coiffeur donné est compatible avec ce client.
     * - Un client sans favori peut être servi par n'importe quel coiffeur.
     * - Un client avec favori ne peut être servi que par son coiffeur favori.
     */
    public boolean estCompatibleAvec(CoiffeurType coiffeur) {
        if (!aFavori) {
            return true; // peut être servi par n'importe qui
        }
        return favori == coiffeur;
    }

    @Override
    public String toString() {
        String favStr = aFavori ? "favori=" + favori.getNom() : "sans favori";
        return "Client#" + id + "(" + favStr + ", " + etat + ")";
    }
}

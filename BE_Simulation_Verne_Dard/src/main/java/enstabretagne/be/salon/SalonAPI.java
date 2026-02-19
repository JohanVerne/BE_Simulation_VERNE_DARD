package enstabretagne.be.salon;

/**
 * Interface exposée par l'entité Salon permettant aux coiffeurs
 * d'interagir avec la file d'attente.
 */
public interface SalonAPI {

    /**
     * Cherche le premier client compatible dans la file d'attente pour le coiffeur donné.
     * Le retire de la file et le renvoie, ou renvoie null si aucun client compatible.
     *
     * @param coiffeur le type de coiffeur qui cherche un client
     * @return le client compatible ou null
     */
    Client prendreClientCompatible(CoiffeurType coiffeur);

    /**
     * Renvoie le nombre total de clients en attente dans la file.
     */
    int getNbClientsEnAttente();

    /**
     * Renvoie le nombre de clients en attente ayant le coiffeur donné comme favori.
     */
    int getNbClientsEnAttenteParFavori(CoiffeurType coiffeur);

    /**
     * Enregistre la fin de service d'un client (il quitte après paiement).
     */
    void clientServi(Client client);

    /**
     * Enregistre un client perdu (parti sans être servi).
     */
    void clientPerdu(Client client);

    /**
     * Notifie le salon qu'un coiffeur est devenu libre (pour tenter
     * d'affecter un nouveau client).
     */
    void notifierCoiffeurLibre(CoiffeurType coiffeur);

    /**
     * Traite l'arrivée d'un nouveau client dans le salon.
     * Vérifie les conditions de départ immédiat ou l'ajoute à la file.
     */
    void traiterArriveeClient(Client client);
}

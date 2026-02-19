package enstabretagne.be.salon;

/**
 * Interface exposée par chaque entité coiffeur,
 * permettant au salon de vérifier la disponibilité d'un coiffeur.
 */
public interface CoiffeurAPI {

    /** Renvoie le type (identité) de ce coiffeur. */
    CoiffeurType getType();

    /** Indique si le coiffeur est présent aujourd'hui. */
    boolean estPresent();

    /** Indique si le coiffeur est actuellement occupé. */
    boolean estOccupe();

    /**
     * Demande au coiffeur de prendre en charge un client.
     * Pré-condition : le coiffeur est présent et libre.
     */
    void prendreEnCharge(Client client);
}

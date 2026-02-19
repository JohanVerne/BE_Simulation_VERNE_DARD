package enstabretagne.be.salon;

/**
 * États possibles d'un client dans le salon.
 */
public enum EtatClient {
    /** Le client attend dans la file d'attente. */
    ATTENTE,
    /** Le client est en train d'être coiffé. */
    PRIS_EN_CHARGE,
    /** Le client paie (durée fixe de 1 minute). */
    PAYE,
    /** Le client a quitté le salon (servi ou non). */
    PARTI
}

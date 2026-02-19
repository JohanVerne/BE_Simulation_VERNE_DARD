package enstabretagne.be.salon;

/**
 * Enumération des coiffeurs du salon.
 * Pétunia est la patronne, Lumpy et Flaky sont les employés.
 */
public enum CoiffeurType {
    PETUNIA("Pétunia"),
    LUMPY("Lumpy"),
    FLAKY("Flaky");

    private final String nom;

    CoiffeurType(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}

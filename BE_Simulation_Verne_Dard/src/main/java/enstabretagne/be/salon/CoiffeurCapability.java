package enstabretagne.be.salon;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.simple.framework.api.SimpleCapability;
import enstabretagne.engine.simple.framework.api.SimpleEntityServices;

/**
 * Capacité d'un coiffeur dans le salon.
 * Gère la présence (absentéisme), l'occupation, et le cycle
 * prise en charge → coupe → libération.
 */
public class CoiffeurCapability implements SimpleCapability, CoiffeurAPI {

    private final CoiffeurType type;
    private final boolean estPatronne; // Pétunia ne peut pas être absente
    private final double probaAbsence;  // probabilité d'absence par jour (1/20 pour employés)
    private final double moyenneCoupe;  // durée moyenne de coupe en minutes
    private final double dureePaiementMinutes; // durée de paiement en minutes

    private SimpleEntityServices owner;
    private boolean present;
    private boolean occupe;
    private Client clientEnCours;

    /**
     * @param type                  identité du coiffeur
     * @param estPatronne           true si c'est Pétunia (toujours présente)
     * @param probaAbsence          probabilité d'absence journalière
     * @param moyenneCoupe          durée moyenne de coupe en minutes (loi exponentielle)
     * @param dureePaiementMinutes  durée de paiement en minutes
     */
    public CoiffeurCapability(CoiffeurType type, boolean estPatronne, double probaAbsence,
                              double moyenneCoupe, double dureePaiementMinutes) {
        this.type = type;
        this.estPatronne = estPatronne;
        this.probaAbsence = probaAbsence;
        this.moyenneCoupe = moyenneCoupe;
        this.dureePaiementMinutes = dureePaiementMinutes;
    }

    @Override
    public void onAttach(SimpleEntityServices owner) {
        this.owner = owner;
        owner.expose(CoiffeurAPI.class, this);
    }

    @Override
    public void onInit() {
        // Décision d'absentéisme en début de journée (Bernoulli)
        if (estPatronne) {
            present = true;
        } else {
            double tirage = owner.random().nextDouble();
            present = tirage >= probaAbsence; // absent si tirage < probaAbsence
        }

        Logger.Information(this, "onInit",
                "%s est %s aujourd'hui", type.getNom(), present ? "PRÉSENT(E)" : "ABSENT(E)");
    }

    @Override
    public CoiffeurType getType() {
        return type;
    }

    @Override
    public boolean estPresent() {
        return present;
    }

    @Override
    public boolean estOccupe() {
        return occupe;
    }

    @Override
    public void prendreEnCharge(Client client) {
        if (!present || occupe) {
            Logger.Error(this, "prendreEnCharge",
                    "%s ne peut pas prendre en charge %s (present=%s, occupe=%s)",
                    type.getNom(), client, present, occupe);
            return;
        }

        this.occupe = true;
        this.clientEnCours = client;
        client.setEtat(EtatClient.PRIS_EN_CHARGE);

        // Durée de coupe selon loi exponentielle de paramètre 1/moyenneCoupe
        double dureeMinutes = owner.random().nextExp(1.0 / moyenneCoupe);
        // Seuil minimum de 1 minute pour éviter les coupes instantanées
        dureeMinutes = Math.max(dureeMinutes, 1.0);
        LogicalDuration dureeCoupe = LogicalDuration.ofMinutes((long) Math.round(dureeMinutes));

        Logger.Information(this, "prendreEnCharge",
                "%s prend en charge %s. Durée prévue: %.1f min",
                type.getNom(), client, dureeMinutes);

        // Planifier la fin de coupe
        owner.scheduleIn(dureeCoupe, this::finCoupe);
    }

    /**
     * Fin de la coupe : le client passe en état PAYE, puis paiement (1 min).
     */
    private void finCoupe() {
        Client client = this.clientEnCours;
        if (client == null) return;

        client.setEtat(EtatClient.PAYE);
        Logger.Information(this, "finCoupe",
                "%s a terminé la coupe de %s. Passage au paiement.",
                type.getNom(), client);

        // Paiement
        LogicalDuration dureePaiement = LogicalDuration.ofMinutes((long) Math.round(dureePaiementMinutes));
        owner.scheduleIn(dureePaiement, () -> finPaiement(client));
    }

    /**
     * Fin du paiement : le client quitte, le coiffeur est libéré.
     */
    private void finPaiement(Client client) {
        client.setEtat(EtatClient.PARTI);
        this.occupe = false;
        this.clientEnCours = null;

        Logger.Information(this, "finPaiement",
                "%s : %s a payé et quitte le salon.",
                type.getNom(), client);

        // Notifier le salon que ce coiffeur est libre
        var salonApis = owner.search(SalonAPI.class);
        if (!salonApis.isEmpty()) {
            salonApis.get(0).clientServi(client);
            salonApis.get(0).notifierCoiffeurLibre(type);
        }
    }

    @Override
    public String toString() {
        return "Coiffeur[" + type.getNom() + "]";
    }
}

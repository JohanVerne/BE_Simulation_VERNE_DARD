package enstabretagne.be.salon;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.simple.framework.api.SimpleCapability;
import enstabretagne.engine.simple.framework.api.SimpleEntityServices;

/**
 * Capacité de génération des arrivées de clients dans le salon.
 * Modélise un processus de Poisson (inter-arrivées exponentielles).
 */
public class ClientGeneratorCapability implements SimpleCapability {

    private final double interArriveMoyenneMinutes; // 12 minutes par défaut
    private final double probaFavori;                // 0.4
    private final double probaFavoriLumpy;           // 0.75 parmi ceux qui ont un favori

    private SimpleEntityServices owner;

    /**
     * @param interArriveMoyenneMinutes durée moyenne entre deux arrivées (minutes)
     * @param probaFavori               probabilité qu'un client ait un favori
     * @param probaFavoriLumpy          probabilité que le favori soit Lumpy (parmi ceux avec favori)
     */
    public ClientGeneratorCapability(double interArriveMoyenneMinutes,
                                     double probaFavori,
                                     double probaFavoriLumpy) {
        this.interArriveMoyenneMinutes = interArriveMoyenneMinutes;
        this.probaFavori = probaFavori;
        this.probaFavoriLumpy = probaFavoriLumpy;
    }

    @Override
    public void onAttach(SimpleEntityServices owner) {
        this.owner = owner;
    }

    @Override
    public void onInit() {
        // Planifier la première arrivée de client
        planifierProchaineArrivee();
    }

    /**
     * Planifie la prochaine arrivée de client selon une loi exponentielle
     * (processus de Poisson).
     */
    private void planifierProchaineArrivee() {
        double interArriveeMinutes = owner.random().nextExp(1.0 / interArriveMoyenneMinutes);
        // Minimum 1 minute entre deux arrivées
        interArriveeMinutes = Math.max(interArriveeMinutes, 1.0);
        LogicalDuration delai = LogicalDuration.ofMinutes((long) Math.round(interArriveeMinutes));

        owner.scheduleIn(delai, this::arriveeClient);
    }

    /**
     * Événement d'arrivée d'un client. Détermine ses caractéristiques
     * et l'envoie au salon.
     */
    private void arriveeClient() {
        // Déterminer si le client a un favori (Bernoulli p=0.4)
        boolean aFavori = owner.random().nextDouble() < probaFavori;

        CoiffeurType favori = null;
        if (aFavori) {
            // Parmi ceux avec favori : 3/4 Lumpy, 1/4 Flaky
            if (owner.random().nextDouble() < probaFavoriLumpy) {
                favori = CoiffeurType.LUMPY;
            } else {
                favori = CoiffeurType.FLAKY;
            }
        }

        Client client = new Client(owner.now(), aFavori, favori);
        Logger.Information(this, "arriveeClient",
                "Arrivée de %s à %s", client, owner.now());

        // Envoyer le client au salon pour traitement
        var salonApis = owner.search(SalonAPI.class);
        if (!salonApis.isEmpty()) {
            salonApis.get(0).traiterArriveeClient(client);
        }

        // Planifier la prochaine arrivée
        planifierProchaineArrivee();
    }

    @Override
    public String toString() {
        return "ClientGenerator";
    }
}

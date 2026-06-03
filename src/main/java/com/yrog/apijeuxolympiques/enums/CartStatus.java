package com.yrog.apijeuxolympiques.enums;

/**
 * Enumération représentant le statut d'un panier.
 */
public enum CartStatus {

    /** Panier en attente de paiement. */
    EN_ATTENTE("En attente"),

    /** Panier payé. */
    PAYE("Payé");

    private final String label;

    CartStatus(String label) {
        this.label = label;
    }

    /**
     * Retourne le libellé lisible du statut.
     */
    public String getLabel() {
        return label;
    }
}


package com.yrog.apijeuxolympiques.dto.stats;

/**
 * Projection JPA représentant les ventes par offre.
 * Utilisée pour les statistiques admin.
 */
public interface SalesByOfferDTO {
    Long getOfferId();
    Long getSalesCount();
}

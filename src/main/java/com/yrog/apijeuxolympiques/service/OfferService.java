package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;
import com.yrog.apijeuxolympiques.dto.offer.OfferDetailDTO;
import java.util.List;

/**
 * Service gérant les opérations sur les offres de billetterie.
 */
public interface OfferService {

    /**
     * Crée une nouvelle offre.
     */
    OfferDTO createOffer(OfferDTO offerDTO);

    /**
     * Retourne toutes les offres.
     */
    List<OfferDTO> getAllOffers();

    /**
     * Retourne une offre par son identifiant.
     */
    OfferDTO getOfferById(Long id);

    /**
     * Met à jour une offre existante.
     */
    OfferDTO updateOffer(Long id, OfferDTO offerDTO);

    /**
     * Supprime une offre par son identifiant.
     */
    void deleteOffer(Long id);

    /**
     * Retourne les détails de toutes les offres.
     * @param adminView si true retourne toutes les offres, sinon seulement les actives
     */
    List<OfferDetailDTO> getAllOffersDetail(boolean adminView);

    /**
     * Met à jour la disponibilité des offres d'un événement.
     */
    void updateOffersAvailabilityByEvent(Long eventId);

    /**
     * Vérifie la disponibilité d'une offre pour une quantité donnée.
     */
    boolean checkAvailabilityForOffer(Long offerId, int requestedQuantity);

    /**
     * Active ou désactive une offre.
     */
    void toggleOfferActive(Long offerId);

    /**
     * Retourne les statistiques de ventes par offre.
     */
    List<OfferDetailDTO> findOfferStats();
}



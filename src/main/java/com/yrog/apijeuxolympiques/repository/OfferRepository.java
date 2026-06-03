package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository gérant les opérations sur les offres de billetterie.
 */
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    /**
     * Retourne toutes les offres d'un événement.
     */
    List<Offer> getAllOffersByEventEventId(Long eventId);
}


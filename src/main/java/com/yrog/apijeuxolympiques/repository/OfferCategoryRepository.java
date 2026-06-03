package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.entity.OfferCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository gérant les opérations sur les catégories d'offres.
 */
@Repository
public interface OfferCategoryRepository extends JpaRepository<OfferCategory, Long> {

    /**
     * Recherche une catégorie par son titre sans tenir compte de la casse.
     */
    Optional<OfferCategory> findByTitleIgnoreCase(String title);
}


package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.offerCategory.OfferCategoryDTO;
import java.util.List;

/**
 * Service gérant les opérations sur les catégories d'offres.
 */
public interface OfferCategoryService {

    /**
     * Crée une nouvelle catégorie d'offre.
     */
    OfferCategoryDTO createCategory(OfferCategoryDTO offerCategoryDTO);

    /**
     * Retourne toutes les catégories d'offres.
     */
    List<OfferCategoryDTO> getAllCategories();

    /**
     * Retourne une catégorie par son identifiant.
     */
    OfferCategoryDTO getCategoryById(Long id);

    /**
     * Met à jour une catégorie existante.
     */
    OfferCategoryDTO updateCategory(Long id, OfferCategoryDTO offerCategoryDTO);

    /**
     * Supprime une catégorie par son identifiant.
     */
    void deleteCategory(Long id);
}



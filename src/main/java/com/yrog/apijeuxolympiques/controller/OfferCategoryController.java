package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.offerCategory.OfferCategoryDTO;
import com.yrog.apijeuxolympiques.service.OfferCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller gérant les catégories d'offres.
 */
@RestController
@RequestMapping("api-jeuxolympiques/offer-categories")
@RequiredArgsConstructor
public class OfferCategoryController {

    private final OfferCategoryService offerCategoryService;

    /**
     * Crée une nouvelle catégorie d'offre.
     */
    @PostMapping
    public ResponseEntity<OfferCategoryDTO> createCategory(@Valid @RequestBody OfferCategoryDTO offerCategoryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(offerCategoryService.createCategory(offerCategoryDTO));
    }

    /**
     * Récupère toutes les catégories d'offres.
     */
    @GetMapping
    public ResponseEntity<List<OfferCategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(offerCategoryService.getAllCategories());
    }

    /**
     * Récupère une catégorie par son identifiant.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OfferCategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(offerCategoryService.getCategoryById(id));
    }

    /**
     * Met à jour une catégorie existante.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OfferCategoryDTO> updateCategory(@PathVariable Long id,
                                                           @Valid @RequestBody OfferCategoryDTO offerCategoryDTO) {
        return ResponseEntity.ok(offerCategoryService.updateCategory(id, offerCategoryDTO));
    }

    /**
     * Supprime une catégorie par son identifiant.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        offerCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}


package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;
import com.yrog.apijeuxolympiques.dto.offer.OfferDetailDTO;
import com.yrog.apijeuxolympiques.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller gérant les opérations sur les offres de billetterie.
 */
@RestController
@RequestMapping("api-jeuxolympiques/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    /**
     * Crée une nouvelle offre.
     */
    @PostMapping
    public ResponseEntity<OfferDTO> createOffer(@Valid @RequestBody OfferDTO offerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(offerService.createOffer(offerDTO));
    }

    /**
     * Récupère les détails de toutes les offres actives.
     */
    @GetMapping
    public ResponseEntity<List<OfferDetailDTO>> getAllOffersDetail() {
        return ResponseEntity.ok(offerService.getAllOffersDetail(false));
    }

    /**
     * Récupère les détails de toutes les offres pour l'admin.
     */
    @GetMapping("/admin")
    public ResponseEntity<List<OfferDetailDTO>> getAllOffersDetailForAdmin() {
        return ResponseEntity.ok(offerService.getAllOffersDetail(true));
    }

    /**
     * Récupère une offre par son identifiant.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getOfferById(id));
    }

    /**
     * Met à jour une offre existante.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long id,
                                                @Valid @RequestBody OfferDTO offerDTO) {
        return ResponseEntity.ok(offerService.updateOffer(id, offerDTO));
    }

    /**
     * Supprime une offre par son identifiant.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Vérifie la disponibilité d'une offre.
     */
    @GetMapping("/{offerId}/check-availability")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable Long offerId,
                                                     @RequestParam int requestedQuantity) {
        return ResponseEntity.ok(offerService.checkAvailabilityForOffer(offerId, requestedQuantity));
    }

    /**
     * Met à jour la disponibilité des offres d'un événement.
     */
    @PatchMapping("/update-offers-availability/event/{eventId}")
    public ResponseEntity<Void> updateOffersAvailabilityByEvent(@PathVariable Long eventId) {
        offerService.updateOffersAvailabilityByEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Active ou désactive une offre.
     */
    @PatchMapping("/{offerId}/toggle-active")
    public ResponseEntity<Void> toggleOfferActive(@PathVariable Long offerId) {
        offerService.toggleOfferActive(offerId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupère les statistiques de ventes par offre.
     */
    @GetMapping("/admin/stats/offers")
    public ResponseEntity<List<OfferDetailDTO>> getFullOfferStats() {
        return ResponseEntity.ok(offerService.findOfferStats());
    }
}



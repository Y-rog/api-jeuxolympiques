package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;
import com.yrog.apijeuxolympiques.dto.offer.OfferDetailAdminRequest;
import com.yrog.apijeuxolympiques.dto.offer.OfferDetailDTO;
import com.yrog.apijeuxolympiques.dto.offer.OfferSalesStatsDTO;
import com.yrog.apijeuxolympiques.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api-jeuxolympiques/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @PostMapping
    public ResponseEntity<OfferDTO> createOffer(@RequestBody OfferDTO offerDTO) {
        OfferDTO createdOffer = offerService.createOffer(offerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOffer);
    }

    @GetMapping()
    public ResponseEntity<List<OfferDetailDTO>> getAllOffersDetail() {
        List<OfferDetailDTO> offers = offerService.getAllOffersDetail();
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<OfferDetailAdminRequest>> getAllOffersDetailForAdmin() {
        List<OfferDetailAdminRequest> offers = offerService.getAllOffersDetailForAdmin();
        return ResponseEntity.ok(offers);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) {
        OfferDTO offerDTO = offerService.getOfferById(id);
        if (offerDTO != null) {
            return ResponseEntity.ok(offerDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long id, @RequestBody OfferDTO offerDTO) {
        OfferDTO updatedOffer = offerService.updateOffer(id, offerDTO);
        if (updatedOffer != null) {
            return ResponseEntity.ok(updatedOffer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        if (offerService.deleteOffer(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoint pour vérifier la disponibilité d'une offre
    @GetMapping("/{offerId}/check-availability")
    public boolean checkAvailability(@PathVariable Long offerId, @RequestParam int requestedQuantity) {
        return offerService.checkAvailabilityForOffer(offerId, requestedQuantity);
    }

    @GetMapping("/update-offers-availability/event/{eventId}")
    public ResponseEntity<Void> updateOffersAvailabilityByEvent(@PathVariable Long eventId) {
        offerService.updateOffersAvailabilityByEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{offerId}/toggle-active")
    public ResponseEntity<Boolean> toggleOfferActive(@PathVariable Long offerId) {
        boolean newStatus = offerService.toggleOfferActive(offerId);
        return ResponseEntity.ok(newStatus);
    }

    @GetMapping("/admin/stats/offers")
    public List<OfferSalesStatsDTO> getFullOfferStats() {
        return offerService.findOfferStats();
    }



}



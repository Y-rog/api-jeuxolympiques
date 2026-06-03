package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;
import com.yrog.apijeuxolympiques.dto.offer.OfferDetailDTO;
import com.yrog.apijeuxolympiques.dto.stats.SalesByOfferDTO;
import com.yrog.apijeuxolympiques.entity.Event;
import com.yrog.apijeuxolympiques.entity.Offer;
import com.yrog.apijeuxolympiques.entity.OfferCategory;
import com.yrog.apijeuxolympiques.mapper.OfferMapper;
import com.yrog.apijeuxolympiques.repository.CartItemRepository;
import com.yrog.apijeuxolympiques.repository.EventRepository;
import com.yrog.apijeuxolympiques.repository.OfferCategoryRepository;
import com.yrog.apijeuxolympiques.repository.OfferRepository;
import com.yrog.apijeuxolympiques.service.EventService;
import com.yrog.apijeuxolympiques.service.OfferService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Implémentation du service gérant les offres de billetterie.
 */
@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferCategoryRepository offerCategoryRepository;
    private final EventRepository eventRepository;
    private final CartItemRepository cartItemRepository;
    private final OfferMapper offerMapper;
    private final EventService eventService;

    /**
     * Convertit une entité Offer en OfferDetailDTO.
     */
    private OfferDetailDTO toOfferDetailDTO(Offer offer, Integer salesCount) {
        return new OfferDetailDTO(
                offer.getOfferId(),
                offer.getPrice(),
                offer.isAvailability(),
                offer.isActive(),
                offer.getEvent().getEventId(),
                offer.getEvent().getEventTitle(),
                offer.getEvent().getEventLocation(),
                offer.getEvent().getEventDateTime(),
                offer.getOfferCategory().getCategoryId(),
                offer.getOfferCategory().getTitle(),
                offer.getOfferCategory().getPlacesPerOffer(),
                salesCount
        );
    }

    @Override
    public OfferDTO createOffer(OfferDTO offerDTO) {
        Offer offer = offerMapper.toEntity(offerDTO);

        OfferCategory category = offerCategoryRepository.findById(offerDTO.offerCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable : " + offerDTO.offerCategoryId()));
        offer.setOfferCategory(category);

        Event event = eventRepository.findById(offerDTO.eventId())
                .orElseThrow(() -> new EntityNotFoundException("Événement introuvable : " + offerDTO.eventId()));
        offer.setEvent(event);

        return offerMapper.toDTO(offerRepository.save(offer));
    }

    @Override
    public List<OfferDTO> getAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(offerMapper::toDTO)
                .toList();
    }

    @Override
    public OfferDTO getOfferById(Long id) {
        return offerMapper.toDTO(
                offerRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Offre introuvable : " + id))
        );
    }

    @Override
    @Transactional
    public OfferDTO updateOffer(Long id, OfferDTO offerDTO) {
        Offer offerToUpdate = offerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable : " + id));

        if (offerToUpdate.isActive()) {
            throw new IllegalStateException("Impossible de modifier une offre publiée.");
        }

        boolean hasSoldTickets = cartItemRepository.existsByOfferAndCart_TransactionUuidIsNotNull(offerToUpdate);

        if (hasSoldTickets) {
            offerToUpdate.setPrice(offerDTO.price());
        } else {
            offerToUpdate.setPrice(offerDTO.price());
            offerToUpdate.setAvailability(offerDTO.availability());

            OfferCategory category = offerCategoryRepository.findById(offerDTO.offerCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable : " + offerDTO.offerCategoryId()));
            offerToUpdate.setOfferCategory(category);

            Event event = eventRepository.findById(offerDTO.eventId())
                    .orElseThrow(() -> new EntityNotFoundException("Événement introuvable : " + offerDTO.eventId()));
            offerToUpdate.setEvent(event);
        }

        return offerMapper.toDTO(offerRepository.save(offerToUpdate));
    }

    @Override
    public void deleteOffer(Long id) {
        Offer offerToDelete = offerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable : " + id));

        if (offerToDelete.isActive()) {
            throw new IllegalStateException("Impossible de supprimer une offre publiée.");
        }

        if (cartItemRepository.existsByOfferAndCart_TransactionUuidIsNotNull(offerToDelete)) {
            throw new IllegalStateException("Impossible de supprimer une offre dont des places ont été vendues.");
        }

        offerRepository.delete(offerToDelete);
    }

    @Override
    public List<OfferDetailDTO> getAllOffersDetail(boolean adminView) {
        return offerRepository.findAll()
                .stream()
                .filter(offer -> adminView || offer.isActive())
                .map(offer -> toOfferDetailDTO(offer, null))
                .toList();
    }

    @Override
    @Transactional
    public boolean checkAvailabilityForOffer(Long offerId, int requestedQuantity) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable : " + offerId));

        int placesRequired = requestedQuantity * offer.getOfferCategory().getPlacesPerOffer();
        int availablePlaces = eventService.getAvailablePlacesForEvent(offer.getEvent().getEventId());
        boolean isAvailable = availablePlaces >= placesRequired;

        if (offer.isAvailability() != isAvailable) {
            offer.setAvailability(isAvailable);
            offerRepository.save(offer);
        }

        return isAvailable;
    }

    @Override
    @Transactional
    public void updateOffersAvailabilityByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Événement introuvable : " + eventId));

        int availablePlaces = eventService.getAvailablePlacesForEvent(eventId);
        int eventCapacity = event.getEventPlacesNumber();

        offerRepository.getAllOffersByEventEventId(event.getEventId())
                .forEach(offer -> {
                    int placesPerOffer = offer.getOfferCategory().getPlacesPerOffer();
                    boolean isAvailable = placesPerOffer <= eventCapacity && availablePlaces >= placesPerOffer;

                    if (offer.isAvailability() != isAvailable) {
                        offer.setAvailability(isAvailable);
                        offerRepository.save(offer);
                    }
                });
    }

    @Override
    public void toggleOfferActive(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable : " + offerId));
        offer.setActive(!offer.isActive());
        offerRepository.save(offer);
    }

    @Override
    public List<OfferDetailDTO> findOfferStats() {
        List<SalesByOfferDTO> salesStats = cartItemRepository.countSalesByOffer();

        Map<Long, Long> salesCountByOfferId = salesStats.stream()
                .collect(Collectors.toMap(SalesByOfferDTO::getOfferId, SalesByOfferDTO::getSalesCount));

        return offerRepository.findAll()
                .stream()
                .map(offer -> toOfferDetailDTO(
                        offer,
                        Math.toIntExact(salesCountByOfferId.getOrDefault(offer.getOfferId(), 0L))
                ))
                .toList();
    }
}
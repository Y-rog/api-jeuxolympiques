package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;
import com.yrog.apijeuxolympiques.dto.offer.OfferDetailAdminRequest;
import com.yrog.apijeuxolympiques.dto.offer.OfferDetailDTO;
import com.yrog.apijeuxolympiques.mapper.impl.OfferMapperImpl;
import com.yrog.apijeuxolympiques.pojo.Event;
import com.yrog.apijeuxolympiques.pojo.Offer;
import com.yrog.apijeuxolympiques.pojo.OfferCategory;
import com.yrog.apijeuxolympiques.repository.CartItemRepository;
import com.yrog.apijeuxolympiques.repository.OfferRepository;
import com.yrog.apijeuxolympiques.repository.OfferCategoryRepository;
import com.yrog.apijeuxolympiques.repository.EventRepository;
import com.yrog.apijeuxolympiques.service.EventService;
import com.yrog.apijeuxolympiques.service.OfferService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferCategoryRepository offerCategoryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OfferMapperImpl offerMapper;

    @Autowired
    private EventService eventService;

    @Override
    public OfferDTO createOffer(OfferDTO offerDTO) {
        Offer offer = offerMapper.toEntity(offerDTO);

        // Lier l'ID de la catégorie
        if (offerDTO.getOfferCategoryId() != null) {
            OfferCategory category = offerCategoryRepository.findById(offerDTO.getOfferCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            offer.setOfferCategory(category);
        }

        // Lier l'ID de l'événement
        if (offerDTO.getEventId() != null) {
            Event event = eventRepository.findById(offerDTO.getEventId())
                    .orElseThrow(() -> new RuntimeException("Event not found"));
            offer.setEvent(event);
        }

        Offer savedOffer = offerRepository.save(offer);
        return offerMapper.toDTO(savedOffer);
    }

    @Override
    public List<Offer> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers.stream().toList();
    }

    @Override
    public OfferDTO getOfferById(Long id) {
        Offer offer = offerRepository.findById(id).orElse(null);
        return offer != null ? offerMapper.toDTO(offer) : null;
    }

    @Override
    @Transactional
    public OfferDTO updateOffer(Long id, OfferDTO offerDTO) {
        Offer offerToUpdate = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        // Si l'offre est active, on interdit la modification
        if (offerToUpdate.isActive()) {
            throw new IllegalStateException("Impossible de modifier une offre publiée.");
        }

        // Vérifie si des tickets ont déjà été vendus pour cette offre
        boolean hasSoldTickets = cartItemRepository.existsByOfferAndCart_TransactionUuidIsNotNull(offerToUpdate);

        if (hasSoldTickets) {
            // Autorise uniquement le changement de prix
            offerToUpdate.setPrice(offerDTO.getPrice());
        } else {
            // Offre modifiable entièrement
            offerToUpdate.setPrice(offerDTO.getPrice());
            offerToUpdate.setAvailability(offerDTO.isAvailability());

            if (offerDTO.getOfferCategoryId() != null) {
                OfferCategory category = offerCategoryRepository.findById(offerDTO.getOfferCategoryId())
                        .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
                offerToUpdate.setOfferCategory(category);
            }

            if (offerDTO.getEventId() != null) {
                Event event = eventRepository.findById(offerDTO.getEventId())
                        .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
                offerToUpdate.setEvent(event);
            }
        }

        Offer updatedOffer = offerRepository.save(offerToUpdate);
        return offerMapper.toDTO(updatedOffer);
    }


    @Override
    public boolean deleteOffer(Long id) {
        Offer offerToDelete = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        // Si l'offre est active, on interdit la suppression
        if (offerToDelete.isActive()) {
            throw new IllegalStateException("Impossible de supprimer une offre publiée.");
        }

        // Vérifie si des tickets ont déjà été vendus pour cette offre
        boolean hasSoldTickets = cartItemRepository.existsByOfferAndCart_TransactionUuidIsNotNull(offerToDelete);

        if (hasSoldTickets) {
            throw new IllegalStateException("Impossible de supprimer une offre dont des places ont été vendues.");
        }

        offerRepository.delete(offerToDelete);
        return true;
    }


    @Override
    public List<OfferDetailDTO> getAllOffersDetail() {
        List<Offer> offers = offerRepository.findAll();

        return offers.stream()
                .filter(Offer::isActive)
                .map(offer -> {
                    OfferDetailDTO dto = new OfferDetailDTO();
                    dto.setOfferId(offer.getOfferId());
                    dto.setPrice(offer.getPrice());
                    dto.setAvailability(offer.isAvailability());
                    dto.setEventId(offer.getEvent().getEventId());
                    dto.setEventTitle(offer.getEvent().getEventTitle());
                    dto.setEventLocation(offer.getEvent().getEventLocation());
                    dto.setEventDateTime(offer.getEvent().getEventDateTime());
                    dto.setOfferCategoryId(offer.getOfferCategory().getCategoryId());
                    dto.setOfferCategoryTitle(offer.getOfferCategory().getTitle());
                    dto.setOfferCategoryPlacesPerOffer(offer.getOfferCategory().getPlacesPerOffer());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OfferDetailAdminRequest> getAllOffersDetailForAdmin() {
        List<Offer> offers = offerRepository.findAll();

        return offers.stream()
                .map(offer -> {
                    OfferDetailAdminRequest request = new OfferDetailAdminRequest();
                    request.setOfferId(offer.getOfferId());
                    request.setPrice(offer.getPrice());
                    request.setAvailability(offer.isAvailability());
                    request.setActive(offer.isActive());
                    request.setEventId(offer.getEvent().getEventId());
                    request.setEventTitle(offer.getEvent().getEventTitle());
                    request.setEventLocation(offer.getEvent().getEventLocation());
                    request.setEventDateTime(offer.getEvent().getEventDateTime());
                    request.setOfferCategoryId(offer.getOfferCategory().getCategoryId());
                    request.setOfferCategoryTitle(offer.getOfferCategory().getTitle());
                    request.setOfferCategoryPlacesPerOffer(offer.getOfferCategory().getPlacesPerOffer());
                    return request;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean checkAvailabilityForOffer(Long offerId, int requestedQuantity) {
        // Récupère l'offre par son ID
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        // Récupère l'événement associé à l'offre
        Event event = offer.getEvent();

        // Récupère la catégorie de l'offre
        OfferCategory offerCategory = offer.getOfferCategory();
        int placesPerOffer = offerCategory.getPlacesPerOffer();

        // Calcule le nombre total de places nécessaires pour la quantité demandée
        int placesRequired = requestedQuantity * placesPerOffer;

        // Calcule le nombre de places encore disponibles pour l'événement
        int availablePlaces = eventService.getAvailablePlacesForEvent(event.getEventId());

        // Détermine si l'offre est disponible (suffisamment de places)
        boolean isAvailable = availablePlaces >= placesRequired;

        // Met à jour la disponibilité de l'offre et la sauvegarde si nécessaire
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
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,"Événement non trouvé avec l'ID : " + eventId));

        int availablePlaces = eventService.getAvailablePlacesForEvent(eventId);
        int eventCapacity = event.getEventPlacesNumber();

        List<Offer> offers = offerRepository.getAllOffersByEventEventId(event.getEventId());

        for (Offer offer : offers) {
            int placesPerOffer = offer.getOfferCategory().getPlacesPerOffer();

            boolean isAvailable;

            // Cas où l'offre demande plus que la capacité totale de l'événement → jamais disponible
            if (placesPerOffer > eventCapacity) {
                isAvailable = false;
            } else {
                // Sinon : disponible si suffisamment de places restantes
                isAvailable = availablePlaces >= placesPerOffer;
            }

            if (offer.isAvailability() != isAvailable) {
                offer.setAvailability(isAvailable);
                offerRepository.save(offer);
            }
        }
    }

    @Override
    public boolean toggleOfferActive(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new EntityNotFoundException("Offre non trouvée"));
        offer.setActive(!offer.isActive());
        offerRepository.save(offer);
        return offer.isActive(); // Retourne le nouvel état
    }


}



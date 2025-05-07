package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;
import com.yrog.apijeuxolympiques.dto.offer.OfferDetailDTO;
import com.yrog.apijeuxolympiques.mapper.impl.OfferMapperImpl;
import com.yrog.apijeuxolympiques.pojo.Event;
import com.yrog.apijeuxolympiques.pojo.Offer;
import com.yrog.apijeuxolympiques.pojo.OfferCategory;
import com.yrog.apijeuxolympiques.repository.OfferRepository;
import com.yrog.apijeuxolympiques.repository.OfferCategoryRepository;
import com.yrog.apijeuxolympiques.repository.EventRepository;
import com.yrog.apijeuxolympiques.service.EventService;
import com.yrog.apijeuxolympiques.service.OfferService;
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
    public OfferDTO updateOffer(Long id, OfferDTO offerDTO) {
        Optional<Offer> existingOffer = offerRepository.findById(id);
        if (existingOffer.isPresent()) {
            Offer offerToUpdate = existingOffer.get();
            offerToUpdate.setPrice(offerDTO.getPrice());
            offerToUpdate.setAvailability(offerDTO.isAvailability());

            if (offerDTO.getOfferCategoryId() != null) {
                OfferCategory category = offerCategoryRepository.findById(offerDTO.getOfferCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"));
                offerToUpdate.setOfferCategory(category);
            }

            if (offerDTO.getEventId() != null) {
                Event event = eventRepository.findById(offerDTO.getEventId())
                        .orElseThrow(() -> new RuntimeException("Event not found"));
                offerToUpdate.setEvent(event);
            }

            Offer updatedOffer = offerRepository.save(offerToUpdate);
            return offerMapper.toDTO(updatedOffer);
        }
        return null;
    }

    @Override
    public boolean deleteOffer(Long id) {
        if (offerRepository.existsById(id)) {
            offerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<OfferDetailDTO> getAllOffersDetail() {
        List<Offer> offers = offerRepository.findAll();

        return offers.stream()
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
}



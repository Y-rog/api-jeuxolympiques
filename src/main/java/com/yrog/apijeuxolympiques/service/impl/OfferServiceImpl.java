package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;
import com.yrog.apijeuxolympiques.mapper.impl.OfferMapperImpl;
import com.yrog.apijeuxolympiques.pojo.Event;
import com.yrog.apijeuxolympiques.pojo.Offer;
import com.yrog.apijeuxolympiques.pojo.OfferCategory;
import com.yrog.apijeuxolympiques.repository.OfferRepository;
import com.yrog.apijeuxolympiques.repository.OfferCategoryRepository;
import com.yrog.apijeuxolympiques.repository.EventRepository;
import com.yrog.apijeuxolympiques.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<OfferDTO> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers.stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());
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
}



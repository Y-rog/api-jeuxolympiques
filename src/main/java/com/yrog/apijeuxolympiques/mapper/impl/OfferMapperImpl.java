package com.yrog.apijeuxolympiques.mapper.impl;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;
import com.yrog.apijeuxolympiques.mapper.OfferMapper;
import com.yrog.apijeuxolympiques.pojo.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferMapperImpl implements OfferMapper {

    @Override
    public OfferDTO toDTO(Offer offer) {
        if (offer == null) {
            return null;
        }

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setPrice(offer.getPrice());
        offerDTO.setAvailability(offer.isAvailability());

        // Vérification que la catégorie n'est pas nulle
        if (offer.getOfferCategory() != null) {
            offerDTO.setOfferCategoryId(offer.getOfferCategory().getCategoryId());
        }

        if (offer.getEvent() != null) {
            offerDTO.setEventId(offer.getEvent().getEventId());
        }

        return offerDTO;
    }

    @Override
    public Offer toEntity(OfferDTO offerDTO) {
        if (offerDTO == null) {
            return null;
        }

        Offer offer = new Offer();
        offer.setPrice(offerDTO.getPrice());
        offer.setAvailability(offerDTO.isAvailability());

        // À implémenter : lier la catégorie et l'événement en fonction des IDs (dans le service)
        return offer;
    }
}



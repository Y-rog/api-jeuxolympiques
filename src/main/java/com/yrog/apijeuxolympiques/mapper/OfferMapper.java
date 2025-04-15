package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;
import com.yrog.apijeuxolympiques.pojo.Offer;

public interface OfferMapper {

    OfferDTO toDTO(Offer offer);  // Convertir une offre en DTO

    Offer toEntity(OfferDTO offerDTO);  // Convertir un DTO en une offre
}


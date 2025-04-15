package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;

import java.util.List;

public interface OfferService {

    OfferDTO createOffer(OfferDTO offerDTO);

    List<OfferDTO> getAllOffers();

    OfferDTO getOfferById(Long id);

    OfferDTO updateOffer(Long id, OfferDTO offerDTO);

    boolean deleteOffer(Long id);
}



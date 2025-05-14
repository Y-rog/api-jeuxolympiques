package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;
import com.yrog.apijeuxolympiques.dto.offer.OfferDetailAdminRequest;
import com.yrog.apijeuxolympiques.dto.offer.OfferDetailDTO;
import com.yrog.apijeuxolympiques.pojo.Offer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OfferService {

    OfferDTO createOffer(OfferDTO offerDTO);

    List<Offer> getAllOffers();

    OfferDTO getOfferById(Long id);

    OfferDTO updateOffer(Long id, OfferDTO offerDTO);

    boolean deleteOffer(Long id);

    List<OfferDetailDTO> getAllOffersDetail();

    @Transactional
    void updateOffersAvailabilityByEvent(Long eventId);

    List<OfferDetailAdminRequest> getAllOffersDetailForAdmin();

    @Transactional
    boolean checkAvailabilityForOffer(Long offerId, int requestedQuantity);

    boolean toggleOfferActive(Long offerId);
}



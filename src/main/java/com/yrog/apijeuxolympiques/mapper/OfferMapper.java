package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.offer.OfferDTO;
import com.yrog.apijeuxolympiques.entity.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct pour convertir entre Offer et OfferDTO.
 */
@Mapper(componentModel = "spring")
public interface OfferMapper {

    /**
     * Convertit un Offer en OfferDTO.
     */
    @Mapping(source = "event.eventId", target = "eventId")
    @Mapping(source = "offerCategory.categoryId", target = "offerCategoryId")
    OfferDTO toDTO(Offer offer);

    /**
     * Convertit un OfferDTO en Offer.
     */
    @Mapping(source = "eventId", target = "event.eventId")
    @Mapping(source = "offerCategoryId", target = "offerCategory.categoryId")
    Offer toEntity(OfferDTO offerDTO);
}


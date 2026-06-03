package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.offerCategory.OfferCategoryDTO;
import com.yrog.apijeuxolympiques.entity.OfferCategory;
import org.mapstruct.Mapper;

/**
 * Mapper MapStruct pour convertir entre OfferCategory et OfferCategoryDTO.
 */
@Mapper(componentModel = "spring")
public interface OfferCategoryMapper {

    /**
     * Convertit un OfferCategory en OfferCategoryDTO.
     */
    OfferCategoryDTO toDTO(OfferCategory offerCategory);

    /**
     * Convertit un OfferCategoryDTO en OfferCategory.
     */
    OfferCategory toEntity(OfferCategoryDTO offerCategoryDTO);
}
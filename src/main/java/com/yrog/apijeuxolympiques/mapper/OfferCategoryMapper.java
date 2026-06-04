package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.offerCategory.OfferCategoryDTO;
import com.yrog.apijeuxolympiques.entity.OfferCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct pour convertir entre OfferCategory et OfferCategoryDTO.
 */
@Mapper(componentModel = "spring")
public interface OfferCategoryMapper {

    /**
     * Convertit un OfferCategory en OfferCategoryDTO.
     */
    @Mapping(source = "categoryId", target = "categoryId")
    OfferCategoryDTO toDTO(OfferCategory offerCategory);

    /**
     * Convertit un OfferCategoryDTO en OfferCategory.
     */
    @Mapping(target = "categoryId", ignore = true)
    OfferCategory toEntity(OfferCategoryDTO offerCategoryDTO);
}
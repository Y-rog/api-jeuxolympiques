package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.offerCategory.OfferCategoryDTO;
import com.yrog.apijeuxolympiques.pojo.OfferCategory;

public interface OfferCategoryMapper {

    OfferCategoryDTO toDTO(OfferCategory offerCategory);  // Convertir une catégorie d'offre en DTO

    OfferCategory toEntity(OfferCategoryDTO offerCategoryDTO);  // Convertir un DTO en une catégorie d'offre
}



package com.yrog.apijeuxolympiques.mapper.impl;

import com.yrog.apijeuxolympiques.dto.offerCategory.OfferCategoryDTO;
import com.yrog.apijeuxolympiques.mapper.OfferCategoryMapper;
import com.yrog.apijeuxolympiques.pojo.OfferCategory;
import org.springframework.stereotype.Component;

@Component
public class OfferCategoryMapperImpl implements OfferCategoryMapper {

    @Override
    public OfferCategoryDTO toDTO(OfferCategory offerCategory) {
        if (offerCategory == null) {
            return null;
        }

        OfferCategoryDTO offerCategoryDTO = new OfferCategoryDTO();
        offerCategoryDTO.setTitle(offerCategory.getTitle());
        offerCategoryDTO.setPlacesPerOffer(offerCategory.getPlacesPerOffer());

        return offerCategoryDTO;
    }

    @Override
    public OfferCategory toEntity(OfferCategoryDTO offerCategoryDTO) {
        if (offerCategoryDTO == null) {
            return null;
        }

        OfferCategory offerCategory = new OfferCategory();
        offerCategory.setTitle(offerCategoryDTO.getTitle());
        offerCategory.setPlacesPerOffer(offerCategoryDTO.getPlacesPerOffer());

        return offerCategory;
    }
}



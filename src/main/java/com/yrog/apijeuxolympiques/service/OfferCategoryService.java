package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.offerCategory.OfferCategoryDTO;
import com.yrog.apijeuxolympiques.pojo.OfferCategory;


import java.util.List;

public interface OfferCategoryService {

    OfferCategoryDTO createCategory(OfferCategoryDTO offerCategoryDTO);

    List<OfferCategory> getAllCategories();

    OfferCategoryDTO getCategoryById(Long id);

    OfferCategoryDTO updateCategory(Long id, OfferCategoryDTO offerCategoryDTO);

    boolean deleteCategory(Long id);
}



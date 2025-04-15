package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.offerCategory.OfferCategoryDTO;
import com.yrog.apijeuxolympiques.mapper.impl.OfferCategoryMapperImpl;
import com.yrog.apijeuxolympiques.pojo.OfferCategory;
import com.yrog.apijeuxolympiques.repository.OfferCategoryRepository;
import com.yrog.apijeuxolympiques.service.OfferCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferCategoryServiceImpl implements OfferCategoryService {

    @Autowired
    private OfferCategoryRepository offerCategoryRepository;

    @Autowired
    private OfferCategoryMapperImpl offerCategoryMapper;

    @Override
    public OfferCategoryDTO createCategory(OfferCategoryDTO offerCategoryDTO) {
        OfferCategory offerCategory = offerCategoryMapper.toEntity(offerCategoryDTO);
        OfferCategory savedOfferCategory = offerCategoryRepository.save(offerCategory);
        return offerCategoryMapper.toDTO(savedOfferCategory);
    }

    @Override
    public List<OfferCategoryDTO> getAllCategories() {
        List<OfferCategory> offerCategories = offerCategoryRepository.findAll();
        return offerCategories.stream()
                .map(offerCategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OfferCategoryDTO getCategoryById(Long id) {
        OfferCategory offerCategory = offerCategoryRepository.findById(id).orElse(null);
        return offerCategory != null ? offerCategoryMapper.toDTO(offerCategory) : null;
    }

    @Override
    public OfferCategoryDTO updateCategory(Long id, OfferCategoryDTO offerCategoryDTO) {
        Optional<OfferCategory> existingCategory = offerCategoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            OfferCategory categoryToUpdate = existingCategory.get();
            categoryToUpdate.setTitle(offerCategoryDTO.getTitle());
            categoryToUpdate.setPlacesPerOffer(offerCategoryDTO.getPlacesPerOffer());

            OfferCategory updatedCategory = offerCategoryRepository.save(categoryToUpdate);
            return offerCategoryMapper.toDTO(updatedCategory);
        }
        return null;
    }

    @Override
    public boolean deleteCategory(Long id) {
        if (offerCategoryRepository.existsById(id)) {
            offerCategoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}



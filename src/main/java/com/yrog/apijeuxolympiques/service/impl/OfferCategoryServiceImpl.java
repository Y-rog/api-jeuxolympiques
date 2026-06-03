package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.offerCategory.OfferCategoryDTO;
import com.yrog.apijeuxolympiques.entity.OfferCategory;
import com.yrog.apijeuxolympiques.mapper.OfferCategoryMapper;
import com.yrog.apijeuxolympiques.repository.OfferCategoryRepository;
import com.yrog.apijeuxolympiques.service.OfferCategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implémentation du service gérant les catégories d'offres.
 */
@Service
@RequiredArgsConstructor
public class OfferCategoryServiceImpl implements OfferCategoryService {

    private final OfferCategoryRepository offerCategoryRepository;
    private final OfferCategoryMapper offerCategoryMapper;

    @Override
    public OfferCategoryDTO createCategory(OfferCategoryDTO dto) {
        offerCategoryRepository.findByTitleIgnoreCase(dto.title())
                .ifPresent(c -> { throw new IllegalArgumentException("Une catégorie avec ce titre existe déjà."); });

        return offerCategoryMapper.toDTO(
                offerCategoryRepository.save(offerCategoryMapper.toEntity(dto))
        );
    }

    @Override
    public List<OfferCategoryDTO> getAllCategories() {
        return offerCategoryRepository.findAll()
                .stream()
                .map(offerCategoryMapper::toDTO)
                .toList();
    }

    @Override
    public OfferCategoryDTO getCategoryById(Long id) {
        return offerCategoryRepository.findById(id)
                .map(offerCategoryMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable : " + id));
    }

    @Override
    public OfferCategoryDTO updateCategory(Long id, OfferCategoryDTO dto) {
        OfferCategory category = offerCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable : " + id));

        category.setTitle(dto.title());
        category.setPlacesPerOffer(dto.placesPerOffer());
        return offerCategoryMapper.toDTO(offerCategoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        if (!offerCategoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Catégorie introuvable : " + id);
        }
        offerCategoryRepository.deleteById(id);
    }
}



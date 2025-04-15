package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.offerCategory.OfferCategoryDTO;
import com.yrog.apijeuxolympiques.service.OfferCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api-jeuxolympiques/offer-categories")
public class OfferCategoryController {

    @Autowired
    private OfferCategoryService offerCategoryService;

    @PostMapping
    public ResponseEntity<OfferCategoryDTO> createCategory(@RequestBody OfferCategoryDTO offerCategoryDTO) {
        OfferCategoryDTO createdCategory = offerCategoryService.createCategory(offerCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @GetMapping
    public ResponseEntity<List<OfferCategoryDTO>> getAllCategories() {
        List<OfferCategoryDTO> categories = offerCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferCategoryDTO> getCategoryById(@PathVariable Long id) {
        OfferCategoryDTO categoryDTO = offerCategoryService.getCategoryById(id);
        if (categoryDTO != null) {
            return ResponseEntity.ok(categoryDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferCategoryDTO> updateCategory(@PathVariable Long id, @RequestBody OfferCategoryDTO offerCategoryDTO) {
        OfferCategoryDTO updatedCategory = offerCategoryService.updateCategory(id, offerCategoryDTO);
        if (updatedCategory != null) {
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (offerCategoryService.deleteCategory(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}


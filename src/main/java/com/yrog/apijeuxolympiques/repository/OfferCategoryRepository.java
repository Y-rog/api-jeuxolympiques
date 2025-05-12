package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.pojo.OfferCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferCategoryRepository extends JpaRepository<OfferCategory, Long> {
    Optional<OfferCategory> findByTitleIgnoreCase(@NotNull(message = "Le titre est obligatoire") @Size(min = 2, max = 30, message = "Le titre doit faire entre 2 et 30 caractères") String title);
}


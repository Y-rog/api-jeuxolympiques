package com.yrog.apijeuxolympiques.dto.offerCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * DTO représentant une catégorie d'offre.
 *
 * @param title          le nom de la catégorie (ex: Solo, Duo, Familiale)
 * @param placesPerOffer le nombre de places incluses dans cette catégorie
 */
@Schema(description = "Catégorie d'offre (ex: Solo, Duo, Familiale)")
public record OfferCategoryDTO(

        @NotNull(message = "Le titre est obligatoire")
        @Size(min = 2, max = 30, message = "Le titre doit faire entre 2 et 30 caractères")
        @Schema(description = "Nom de la catégorie", example = "Trio")
        String title,

        @NotNull(message = "Le nombre de places par offre est obligatoire")
        @Min(value = 1, message = "Il faut au moins 1 place par offre")
        @Max(value = 10, message = "Le nombre maximum de places par offre est 10")
        @Schema(description = "Nombre de places incluses dans une offre de cette catégorie", example = "3")
        Integer placesPerOffer

) {}




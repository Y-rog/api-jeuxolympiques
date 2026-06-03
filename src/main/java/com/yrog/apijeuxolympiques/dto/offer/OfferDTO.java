package com.yrog.apijeuxolympiques.dto.offer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO représentant les détails d'une offre de billets.
 *
 * @param price           le prix de l'offre
 * @param availability    la disponibilité de l'offre
 * @param active          l'activation de l'offre
 * @param eventId         l'identifiant de l'événement lié
 * @param offerCategoryId l'identifiant de la catégorie
 */
@Schema(description = "Détails d'une offre de billets")
public record OfferDTO(

        @NotNull(message = "Le prix est obligatoire")
        @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
        @Schema(description = "Prix total de l'offre", example = "40.0")
        BigDecimal price,

        @Schema(description = "Disponibilité de l'offre", example = "true")
        boolean availability,

        @Schema(description = "Activation de l'offre", example = "true")
        boolean active,

        @NotNull(message = "L'identifiant de l'événement est obligatoire")
        @Schema(description = "Identifiant de l'événement lié", example = "10")
        Long eventId,

        @NotNull(message = "L'identifiant de la catégorie est obligatoire")
        @Schema(description = "Identifiant de la catégorie (solo, duo...)", example = "2")
        Long offerCategoryId

) {}




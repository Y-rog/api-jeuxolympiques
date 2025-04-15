package com.yrog.apijeuxolympiques.dto.offer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Détails d'une offre de billets")
public class OfferDTO {

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    @Schema(description = "Prix total de l'offre", example = "40.0")
    private Double price;

    @Schema(description = "Disponibilité de l'offre (true = disponible, false = épuisée)", example = "true")
    private boolean availability;

    @NotNull(message = "L'identifiant de l'événement est obligatoire")
    @Schema(description = "Identifiant de l'événement lié", example = "10")
    private Long eventId;

    @NotNull(message = "L'identifiant de la catégorie est obligatoire")
    @Schema(description = "Identifiant de la catégorie (solo, duo...)", example = "2")
    private Long offerCategoryId;

    public OfferDTO() {}
}




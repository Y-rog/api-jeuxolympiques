package com.yrog.apijeuxolympiques.dto.offer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Détails enrichis d'une offre incluant les infos de l'événement et de la catégorie")
public class OfferDetailDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    @Schema(description = "Prix de l'offre", example = "49.99")
    @NotNull(message = "Le prix ne peut pas être nul")
    private BigDecimal price;

    @Schema(description = "Disponibilité de l'offre", example = "true")
    private boolean availability;

    @Schema(description = "ID de l'événement lié", example = "1")
    @NotNull
    private Long eventId;

    @Schema(description = "Titre de l'événement", example = "Finale de Basketball")
    private String eventTitle;

    @Schema(description = "Lieu de l'événement", example = "Stade de Lille")
    private String eventLocation;

    @Schema(description = "Date et heure de l'événement (format lisible)", example = "22/07/2024 20:00")
    private LocalDateTime eventDateTime; // ici on utilise String formaté, sinon tu peux mettre LocalDateTime

    @Schema(description = "ID de la catégorie d'offre", example = "2")
    @NotNull
    private Long offerCategoryId;

    @Schema(description = "Titre de la catégorie d'offre", example = "Duo")
    private String offerCategoryTitle;

    public OfferDetailDTO() {}
}


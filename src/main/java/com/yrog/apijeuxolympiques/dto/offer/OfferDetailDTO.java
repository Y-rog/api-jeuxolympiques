package com.yrog.apijeuxolympiques.dto.offer;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO représentant les détails complets d'une offre.
 * Utilisé pour les vues utilisateur et administrateur.
 *
 * @param offerId                     l'identifiant de l'offre
 * @param price                       le prix de l'offre
 * @param availability                la disponibilité de l'offre
 * @param active                      l'activation de l'offre
 * @param eventId                     l'identifiant de l'événement lié
 * @param eventTitle                  le titre de l'événement
 * @param eventLocation               le lieu de l'événement
 * @param eventDateTime               la date et heure de l'événement
 * @param offerCategoryId             l'identifiant de la catégorie d'offre
 * @param offerCategoryTitle          le titre de la catégorie
 * @param offerCategoryPlacesPerOffer le nombre de places par offre
 * @param salesCount                  le nombre de ventes (null si non disponible)
 */
@Schema(description = "Détails complets d'une offre")
public record OfferDetailDTO(
        Long offerId,
        BigDecimal price,
        boolean availability,
        boolean active,
        Long eventId,
        String eventTitle,
        String eventLocation,
        LocalDateTime eventDateTime,
        Long offerCategoryId,
        String offerCategoryTitle,
        Integer offerCategoryPlacesPerOffer,
        Integer salesCount
) {}

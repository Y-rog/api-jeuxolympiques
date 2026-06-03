package com.yrog.apijeuxolympiques.dto.ticket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO représentant un ticket acheté avec ses détails.
 *
 * @param cartItemId         l'identifiant de l'article du panier
 * @param qrCode             le QR code unique du ticket
 * @param priceAtPurchase    le prix au moment de l'achat
 * @param eventTitle         le titre de l'événement
 * @param eventLocation      le lieu de l'événement
 * @param eventDateTime      la date et heure de l'événement
 * @param offerCategoryTitle le titre de la catégorie d'offre
 * @param placesPerOffer     le nombre de places incluses
 */
public record TicketResponse(
        Long cartItemId,
        String qrCode,
        BigDecimal priceAtPurchase,
        String eventTitle,
        String eventLocation,
        LocalDateTime eventDateTime,
        String offerCategoryTitle,
        Integer placesPerOffer
) {}

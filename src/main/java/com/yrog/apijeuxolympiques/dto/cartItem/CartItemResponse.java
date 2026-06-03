package com.yrog.apijeuxolympiques.dto.cartItem;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * DTO représentant la réponse d'un article du panier.
 *
 * @param cartItemId      l'identifiant de l'article
 * @param qrCode          le QR code généré pour cet article
 * @param priceAtPurchase le prix au moment de l'achat
 * @param offerId         l'identifiant de l'offre liée
 * @param cartId          l'identifiant du panier
 * @param addedAt         la date d'ajout au panier
 * @param expirationTime  la date d'expiration de l'article
 */
public record CartItemResponse(
        Long cartItemId,
        String qrCode,
        BigDecimal priceAtPurchase,
        Long offerId,
        Long cartId,
        LocalDateTime addedAt,
        Instant expirationTime
) {}
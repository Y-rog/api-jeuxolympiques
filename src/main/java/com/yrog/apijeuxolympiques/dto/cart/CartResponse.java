package com.yrog.apijeuxolympiques.dto.cart;

import com.yrog.apijeuxolympiques.enums.CartStatus;


import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO représentant la réponse d'un panier.
 *
 * @param cartId           l'identifiant unique du panier
 * @param status           le statut du panier (EN_ATTENTE, PAYE...)
 * @param amount           le montant total du panier
 * @param createdAt        la date de création du panier
 * @param updatedAt        la date de dernière mise à jour
 * @param transactionUuid  l'UUID de la transaction de paiement
 * @param dateTransaction  la date de la transaction
 * @param userId           l'identifiant de l'utilisateur propriétaire
 */
public record CartResponse (
        Long cartId,
        CartStatus status,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String transactionUuid,
        LocalDateTime dateTransaction,
        Long userId
) {}
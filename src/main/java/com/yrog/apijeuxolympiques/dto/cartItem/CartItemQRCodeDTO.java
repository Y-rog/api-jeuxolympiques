package com.yrog.apijeuxolympiques.dto.cartItem;

/**
 * DTO représentant un article du panier avec son QR code.
 *
 * @param cartItemId l'identifiant de l'article
 * @param qrCode     le QR code généré pour cet article
 */
public record CartItemQRCodeDTO(
        Long cartItemId,
        String qrCode
) {}


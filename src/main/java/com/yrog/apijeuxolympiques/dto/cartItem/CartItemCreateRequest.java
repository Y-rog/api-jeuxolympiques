package com.yrog.apijeuxolympiques.dto.cartItem;

import jakarta.validation.constraints.*;

/**
 * DTO représentant la requête d'ajout d'un article au panier.
 *
 * @param offerId          l'identifiant de l'offre liée à cet article
 */
public record CartItemCreateRequest(

        @NotNull(message = "L'identifiant de l'offre est obligatoire")
        Long offerId

) {}



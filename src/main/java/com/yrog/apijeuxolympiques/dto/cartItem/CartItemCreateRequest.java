package com.yrog.apijeuxolympiques.dto.cartItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class CartItemCreateRequest {
    @NotNull(message = "L'identifiant de l'offre est obligatoire")
    @Schema(description = "Identifiant de l’offre liée à cet élément", example = "3")
    private Long offerId;

    @DecimalMin(value = "0.0", inclusive = true, message = "Le prix doit être positif")
    @Schema(description = "Prix de l’offre au moment de l’achat", example = "24.99")
    private BigDecimal priceAtPurchase;

    @Schema(
            description = "Heure d'expiration de l'élément dans le panier",
            example = "2025-04-22T15:30:00Z",
            type = "string",
            format = "date-time"
    )
    private Instant expirationTime;
}



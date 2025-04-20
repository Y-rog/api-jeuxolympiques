package com.yrog.apijeuxolympiques.dto.cartItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateRequest {

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au minimum de 1")
    @Max(value = 100, message = "La quantité maximale autorisée est 100")
    @Schema(description = "Quantité choisie de cette offre", example = "2")
    private Integer quantity;
}

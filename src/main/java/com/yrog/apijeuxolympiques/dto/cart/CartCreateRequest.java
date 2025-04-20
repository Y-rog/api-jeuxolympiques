package com.yrog.apijeuxolympiques.dto.cart;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.enums.CartStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CartCreateRequest {
    @NotNull(message = "Le statut est obligatoire")
    @Schema(description = "Statut du panier", example = "EN_ATTENTE")
    private CartStatus status;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le montant doit être positif")
    @Schema(description = "Montant total du panier", example = "49.99")
    private BigDecimal amount;

    @Schema(description = "Date de création du panier", example = "2025-04-19T17:10:00")
    private LocalDateTime createdAt;

    @Schema(description = "Date de mise à jour du panier", example = "2025-04-19T17:11:00")
    private LocalDateTime updatedAt;

    @Size(max = 255, message = "L’UUID de transaction ne peut pas dépasser 255 caractères")
    @Schema(description = "UUID de la transaction", example = "b20a4be7-5b8f-4c18-baf9-213c4e0c2ff8")
    private String transactionUuid;

    @Schema(description = "Date de la transaction", example = "2025-04-20T10:00:00")
    private LocalDateTime dateTransaction;

    private Long userId;

    @Schema(description = "Liste des éléments du panier")
    private List<@Valid CartItemCreateRequest> items;
}


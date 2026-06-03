package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.cart.CartResponse;
import com.yrog.apijeuxolympiques.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller gérant les opérations sur les paniers.
 */
@RestController
@RequestMapping("api-jeuxolympiques/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Crée un nouveau panier pour l'utilisateur connecté.
     */
    @PostMapping
    public ResponseEntity<CartResponse> createCart() {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.createCart());
    }

    /**
     * Récupère le panier actif de l'utilisateur connecté.
     */
    @GetMapping
    public ResponseEntity<CartResponse> findCartByUser() {
        return ResponseEntity.ok(cartService.findCartByUser());
    }

    /**
     * Récupère un panier par son identifiant.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    /**
     * Supprime un panier par son identifiant.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Confirme ou simule l'échec du paiement d'un panier.
     */
    @PostMapping("/{id}/confirm-payment")
    public ResponseEntity<String> confirmPayment(@PathVariable Long id,
                                                 @RequestParam boolean simulateFailure) {
        cartService.confirmPaymentAndGenerateQRCode(id, simulateFailure);
        return ResponseEntity.ok("Paiement " + (simulateFailure ? "échoué" : "validé") + " avec succès.");
    }
}



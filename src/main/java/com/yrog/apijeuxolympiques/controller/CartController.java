package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.cart.CartCreateRequest;
import com.yrog.apijeuxolympiques.dto.cart.CartResponse;
import com.yrog.apijeuxolympiques.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api-jeuxolympiques/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Créer un panier
    @PostMapping
    public ResponseEntity<CartResponse> createCart(@RequestBody CartCreateRequest request) {
        CartResponse response = cartService.createCart(request);
        return ResponseEntity.status(201).body(response);
    }

    // Trouver le panier de l'utilisateur
    @GetMapping
    public ResponseEntity<CartResponse> findCartByUser() {
        CartResponse cartResponse = cartService.findCartByUser(); // On récupère un DTO
        if (cartResponse == null) {
            return ResponseEntity.notFound().build(); // Retourne un 404 si le panier n'est pas trouvé
        }
        return ResponseEntity.ok(cartResponse);
    }

    // Récupérer un panier par son ID
    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCartById(@PathVariable Long id) {
        CartResponse cartResponse = cartService.getCartById(id); // On retourne un DTO ici aussi
        if (cartResponse == null) {
            return ResponseEntity.notFound().build(); // Retourne un 404 si panier introuvable
        }
        return ResponseEntity.ok(cartResponse);
    }

    // Supprimer un panier par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        try {
            cartService.deleteCart(id); // Appelle la méthode qui supprime le panier
            return ResponseEntity.noContent().build(); // 204 No Content, suppression réussie
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found si panier introuvable
        }
    }

}



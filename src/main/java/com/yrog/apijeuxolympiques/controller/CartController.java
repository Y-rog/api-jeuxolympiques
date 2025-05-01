package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.cart.CartCreateRequest;
import com.yrog.apijeuxolympiques.dto.cart.CartResponse;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api-jeuxolympiques/cart")
public class CartController {


    @Autowired
    private CartService cartService;

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

    @PostMapping("/{id}/confirm-payment")
    public ResponseEntity<String> confirmPayment(@PathVariable Long id, @RequestParam boolean simulateFailure) {
        try {
            cartService.confirmPaymentAndGenerateQRCode(id, simulateFailure);
            return ResponseEntity.ok("Paiement " + (simulateFailure ? "échoué" : "validé") + " avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du paiement : " + e.getMessage());
        }
    }





}



package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller gérant les articles du panier.
 */
@RestController
@RequestMapping("api-jeuxolympiques/cart/{cartId}/items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    /**
     * Récupère tous les articles d'un panier.
     */
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> findCartItemsByCartId(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartItemService.findCartItemsByCartId(cartId));
    }

    /**
     * Ajoute un article au panier via RabbitMQ.
     */
    @PostMapping
    public ResponseEntity<Void> addItem(@PathVariable Long cartId,
                                        @RequestBody @Valid CartItemCreateRequest request) {
        cartItemService.addItemToCart(cartId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Supprime un article du panier.
     */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long cartId,
                                                   @PathVariable Long cartItemId) {
        cartItemService.removeItemFromCart(cartId, cartItemId);
        return ResponseEntity.noContent().build();
    }
}


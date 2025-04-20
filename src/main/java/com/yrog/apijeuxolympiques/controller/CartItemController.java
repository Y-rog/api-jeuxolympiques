package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.service.CartItemService;
import com.yrog.apijeuxolympiques.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api-jeuxolympiques/cart/{cartId}/items")
public class CartItemController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    public CartItemController(CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    // Récupérer les éléments du panier par cartId
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> findCartItemsByCartId(@PathVariable Long cartId) {
        List<CartItemResponse> items = cartItemService.findCartItemsByCartId(cartId);

        // Si aucun élément n'est trouvé dans le panier
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(items);
    }

    // Supprimer un élément du panier
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long cartItemId) {
        cartItemService.removeItemFromCart(cartId, cartItemId);
        return ResponseEntity.noContent().build();  // Renvoie un code HTTP 204 (No Content)
    }

    // Ajouter un élément dans le panier
    @PostMapping
    public ResponseEntity<CartItemResponse> addItem(
            @PathVariable Long cartId,
            @RequestBody @Valid CartItemCreateRequest request) {

        CartItemResponse response = cartItemService.addItemToCart(cartId, request);
        return ResponseEntity.ok(response);  // Renvoie le CartItemResponse avec un code HTTP 200
    }
}


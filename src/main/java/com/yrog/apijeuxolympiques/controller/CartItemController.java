package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemUpdateRequest;
import com.yrog.apijeuxolympiques.service.CartItemService;
import com.yrog.apijeuxolympiques.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api-jeuxolympiques/cart/{cartId}/items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartService cartService, CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    // Récupérer les éléments du panier par cartId
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> findCartItemsByCartId(@PathVariable Long cartId) {
        List<CartItemResponse> items = cartItemService.findCartItemsByCartId(cartId);

        // Si aucun élément n'est trouvé dans le panier, on renvoie un tableau vide avec un statut 200 OK
        if (items.isEmpty()) {
            return ResponseEntity.ok().body(List.of());  // Réponse avec un tableau vide
        }

        return ResponseEntity.ok(items);  // Retourne les éléments du panier si trouvés
    }

    // Supprimer un élément du panier
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long cartItemId) {
        cartItemService.removeItemFromCart(cartId, cartItemId);
        return ResponseEntity.noContent().build();  // Renvoie un code HTTP 204 (No Content)
    }

    // Ajouter un élément dans le panier
    @PostMapping
    public ResponseEntity<CartItemResponse> addItem(@PathVariable Long cartId, @RequestBody @Valid CartItemCreateRequest request) {

        CartItemResponse response = cartItemService.addItemToCart(cartId, request);
        return ResponseEntity.ok(response);  // Renvoie le CartItemResponse avec un code HTTP 200
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<?> updateItemQuantity(@PathVariable Long cartId,
                                                @PathVariable Long cartItemId,
                                                @RequestBody CartItemUpdateRequest request) {

        CartItemResponse updatedItem = cartItemService.updateItemQuantity(cartId, cartItemId, request);
        return ResponseEntity.ok(updatedItem);
    }
}


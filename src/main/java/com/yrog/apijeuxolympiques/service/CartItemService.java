package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.entity.CartItem;
import java.util.List;

/**
 * Service gérant les opérations sur les articles du panier.
 */
public interface CartItemService {

    /**
     * Retourne tous les articles d'un panier.
     */
    List<CartItemResponse> findCartItemsByCartId(Long cartId);

    /**
     * Ajoute un article au panier via RabbitMQ.
     */
    void addItemToCart(Long cartId, CartItemCreateRequest request);

    /**
     * Supprime un article du panier.
     */
    void removeItemFromCart(Long cartId, Long cartItemId);

    /**
     * Retourne un article du panier par son identifiant.
     */
    CartItem getCartItem(Long cartItemId);
}

package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.cart.CartResponse;
import com.yrog.apijeuxolympiques.entity.Cart;

/**
 * Service gérant les opérations sur les paniers.
 */
public interface CartService {

    /**
     * Crée un nouveau panier pour l'utilisateur connecté.
     */
    CartResponse createCart(); // ← plus de paramètre !

    /**
     * Récupère un panier par son identifiant.
     */
    CartResponse getCartById(Long id);

    /**
     * Supprime un panier par son identifiant.
     */
    void deleteCart(Long id);

    /**
     * Récupère le panier actif de l'utilisateur connecté.
     */
    CartResponse findCartByUser();

    /**
     * Met à jour le montant total du panier.
     */
    void updateCartAmount(Cart cart);

    /**
     * Confirme le paiement et génère les QR codes.
     */
    void confirmPaymentAndGenerateQRCode(Long cartId, boolean simulateFailure);

    /**
     * Simule un paiement échoué.
     */
    Cart simulateFailedPayment(Long cartId);

    /**
     * Valide le paiement du panier.
     */
    Cart validatePayment(Long cartId);

    /**
     * Génère les QR codes pour les articles du panier.
     */
    void generateKeyConcatenationCartItems(Cart cart);
}


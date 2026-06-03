package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.cart.CartResponse;
import com.yrog.apijeuxolympiques.enums.CartStatus;
import com.yrog.apijeuxolympiques.mapper.CartMapper;
import com.yrog.apijeuxolympiques.entity.Cart;
import com.yrog.apijeuxolympiques.entity.CartItem;
import com.yrog.apijeuxolympiques.entity.User;
import com.yrog.apijeuxolympiques.repository.CartItemRepository;
import com.yrog.apijeuxolympiques.repository.CartRepository;
import com.yrog.apijeuxolympiques.repository.UserRepository;
import com.yrog.apijeuxolympiques.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation du service gérant les opérations sur les paniers.
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * Récupère l'utilisateur authentifié depuis le contexte de sécurité.
     *
     * @return l'utilisateur connecté
     * @throws AccessDeniedException si l'utilisateur n'est pas authentifié
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Accès refusé");
        }
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
    }

    @Override
    public CartResponse createCart() {
        User user = getAuthenticatedUser();

        Optional<Cart> existingCart = cartRepository.findByUserAndStatus(user, CartStatus.EN_ATTENTE);
        if (existingCart.isPresent()) {
            throw new IllegalStateException("Un panier actif existe déjà pour cet utilisateur.");
        }

        Cart cart = new Cart();
        cart.setStatus(CartStatus.EN_ATTENTE);
        cart.setUser(user);

        return cartMapper.toResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse getCartById(Long id) {
        return cartMapper.toResponse(
                cartRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Panier introuvable : " + id))
        );
    }

    @Override
    public void deleteCart(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new EntityNotFoundException("Panier introuvable : " + id);
        }
        cartRepository.deleteById(id);
    }

    @Override
    public CartResponse findCartByUser() {
        User user = getAuthenticatedUser();

        Optional<Cart> optionalCart = cartRepository.findByUserAndStatus(user, CartStatus.EN_ATTENTE);

        Cart cart = optionalCart.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setStatus(CartStatus.EN_ATTENTE);
            return cartRepository.save(newCart);
        });

        return cartMapper.toResponse(cart);
    }

    @Override
    public void updateCartAmount(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getPriceAtPurchase)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setAmount(total);
    }

    @Override
    @Transactional
    public void confirmPaymentAndGenerateQRCode(Long cartId, boolean simulateFailure) {
        Cart cart = simulateFailure ? simulateFailedPayment(cartId) : validatePayment(cartId);
        if (!simulateFailure) {
            generateKeyConcatenationCartItems(cart);
        }
    }

    @Override
    public Cart simulateFailedPayment(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Panier introuvable : " + cartId));

        cart.setStatus(CartStatus.EN_ATTENTE);
        cart.setDateTransaction(null);
        cart.setTransactionUuid(null);

        return cartRepository.save(cart);
    }

    @Override
    public Cart validatePayment(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Panier introuvable : " + cartId));

        cart.setDateTransaction(LocalDateTime.now());
        cart.setTransactionUuid(UUID.randomUUID().toString());
        cart.setStatus(CartStatus.PAYE);

        return cartRepository.save(cart);
    }

    @Override
    public void generateKeyConcatenationCartItems(Cart cart) {
        User user = cart.getUser();
        if (user == null || user.getSecretKey() == null) {
            throw new IllegalStateException("Utilisateur ou clé secrète manquante pour le panier.");
        }

        for (CartItem item : cart.getItems()) {
            item.setQrCode(cart.getTransactionUuid() + "_" + user.getSecretKey());
        }

        cartItemRepository.saveAll(cart.getItems());
    }
}


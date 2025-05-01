package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.cart.CartCreateRequest;
import com.yrog.apijeuxolympiques.enums.CartStatus;
import com.yrog.apijeuxolympiques.mapper.CartMapper;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import com.yrog.apijeuxolympiques.repository.CartItemRepository;
import com.yrog.apijeuxolympiques.repository.CartRepository;
import com.yrog.apijeuxolympiques.security.models.User;
import com.yrog.apijeuxolympiques.security.repository.UserRepository;
import com.yrog.apijeuxolympiques.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.yrog.apijeuxolympiques.dto.cart.CartResponse;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public CartResponse createCart(CartCreateRequest request) {
        // Récupération de l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Access denied");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Vérifie si un panier EN_ATTENTE existe déjà pour l'utilisateur
        Optional<Cart> existingCart = cartRepository.findByUserAndStatus(user, CartStatus.EN_ATTENTE);
        if (existingCart.isPresent()) {
            throw new IllegalStateException("Un panier actif existe déjà pour cet utilisateur.");
        }

        // Mapper la requête vers une entité Cart
        Cart cart = cartMapper.toEntity(request);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setStatus(CartStatus.EN_ATTENTE); // ou autre statut par défaut
        cart.setUser(user);

        // Sauvegarde et retour
        return cartMapper.toResponse(cartRepository.save(cart));
    }


    @Override
    public CartResponse getCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        // Convertir le Cart en CartResponse
        return cartMapper.toResponse(cart);
    }

    @Override
    public void deleteCart(Long id) {
        // Vérifier si le panier existe
        if (!cartRepository.existsById(id)) {
            throw new EntityNotFoundException("Panier non trouvé");
        }

        // Supprimer le panier
        cartRepository.deleteById(id);
    }


    @Override
    public CartResponse findCartByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Accès refusé");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // Ne chercher que les paniers avec statut EN_ATTENTE
        Optional<Cart> optionalCart = cartRepository.findByUserAndStatus(user, CartStatus.EN_ATTENTE);

        Cart cart;
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            // Aucun panier EN_ATTENTE : on en crée un nouveau
            cart = new Cart();
            cart.setUser(user);
            cart.setCreatedAt(LocalDateTime.now());
            cart.setStatus(CartStatus.EN_ATTENTE);
            cart = cartRepository.save(cart);
        }

        return cartMapper.toResponse(cart);
    }



    @Override
    public void updateCartAmount(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPriceAtPurchase())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setAmount(total);
    }


    @Override
    @Transactional
    public void confirmPaymentAndGenerateQRCode(Long cartId, boolean simulateFailure) {
        Cart cart = simulateFailure ? simulateFailedPayment(cartId) : validatePayment(cartId);
        if (!simulateFailure) {
            generateQRCodeCartItems(cart);
        }
    }

    @Override
    public Cart simulateFailedPayment(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.setStatus(CartStatus.EN_ATTENTE);
        cart.setDateTransaction(null);
        cart.setTransactionUuid(null);

        cartRepository.save(cart);

        return cart;
    }

    @Override
    public Cart validatePayment(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.setDateTransaction(LocalDateTime.now());
        cart.setTransactionUuid(UUID.randomUUID().toString());
        cart.setStatus(CartStatus.PAYE);

        return cartRepository.save(cart);
    }

    @Override
    public void generateQRCodeCartItems(Cart cart) {
        User user = cart.getUser();
        if (user == null || user.getSecretKey() == null) {
            throw new RuntimeException("Utilisateur ou clé secrète manquante pour le panier.");
        }

        for (CartItem item : cart.getItems()) {
            String cleFinale = cart.getTransactionUuid() + "_" + user.getSecretKey();
            item.setQrCode(cleFinale);
        }

        cartItemRepository.saveAll(cart.getItems());
    }

}


package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.cart.CartCreateRequest;
import com.yrog.apijeuxolympiques.enums.CartStatus;
import com.yrog.apijeuxolympiques.mapper.CartMapper;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.repository.CartRepository;
import com.yrog.apijeuxolympiques.security.models.User;
import com.yrog.apijeuxolympiques.security.repository.UserRepository;
import com.yrog.apijeuxolympiques.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.yrog.apijeuxolympiques.dto.cart.CartResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final UserRepository userRepository;

    public CartServiceImpl(
            CartRepository cartRepository,
            CartMapper cartMapper,
            UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.userRepository = userRepository;
    }

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
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Accès refusé");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // Trouver le panier associé à cet utilisateur
        Optional<Cart> optionalCart = cartRepository.findByUser(user);

        Cart cart;
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            // Créer un nouveau panier vide
            cart = new Cart();
            cart.setUser(user);
            cart.setCreatedAt(LocalDateTime.now());
            cart.setStatus(CartStatus.EN_ATTENTE); // ou autre valeur par défaut
            cart = cartRepository.save(cart); // Sauvegarde en base
        }


        // Retourner le Cart en CartResponse
        return cartMapper.toResponse(cart);
    }

    public void updateCartAmount(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setAmount(total);
    }



}


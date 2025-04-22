package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.mapper.CartItemMapper;
import com.yrog.apijeuxolympiques.mapper.CartMapper;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import com.yrog.apijeuxolympiques.pojo.Offer;
import com.yrog.apijeuxolympiques.repository.CartItemRepository;
import com.yrog.apijeuxolympiques.repository.CartRepository;
import com.yrog.apijeuxolympiques.repository.OfferRepository;
import com.yrog.apijeuxolympiques.security.repository.UserRepository;
import com.yrog.apijeuxolympiques.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final OfferRepository offerRepository;

    @Value("${cart.item.expiration-duration}")
    private int expirationDuration;

    public CartItemServiceImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            CartMapper cartMapper,
            CartItemMapper cartItemMapper,
            OfferRepository offerRepository,
            UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
        this.offerRepository = offerRepository;
    }

    @Override
    public List<CartItemResponse> findCartItemsByCartId(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        List<CartItem> items = cartItemRepository.findByCart(cart);
        return cartItemMapper.toResponseList(items);
    }

    @Override
    public CartItemResponse addItemToCart(Long cartId, CartItemCreateRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        Offer offer = offerRepository.findById(request.getOfferId())
                .orElseThrow(() -> new EntityNotFoundException("Offer not found"));

        Instant expirationTime = Instant.now().plus(Duration.ofMinutes(expirationDuration));

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setOffer(offer);
        item.setQuantity(request.getQuantity());
        item.setPriceAtPurchase(request.getPriceAtPurchase());
        item.setQrCode(UUID.randomUUID().toString());
        item.setExpirationTime(expirationTime);

        cartItemRepository.save(item);

        cart.getItems().add(item);
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setAmount(cart.getItems().stream()
                .map(i -> i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity())))  // Multiplier le prix par la quantité
                .reduce(BigDecimal.ZERO, BigDecimal::add)  // Additionner toutes les valeurs obtenues
        );

        cartRepository.save(cart);


        // Retour DTO
        return cartItemMapper.toResponse(item); // ou map manuellement comme montré au-dessus
    }


    @Override
    public void removeItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem itemToRemove = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Supprimer l'item du panier
        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);

        // Recalculer le montant total du panier
        cart.setAmount(cart.getItems().stream()
                .map(i -> i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity()))) // Multiplier en utilisant BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add)); // Additionner les résultats


        // Sauvegarder le panier mis à jour
        cartRepository.save(cart);
    }
}

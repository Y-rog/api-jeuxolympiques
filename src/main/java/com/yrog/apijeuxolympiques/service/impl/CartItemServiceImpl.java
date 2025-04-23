package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemUpdateRequest;
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
import com.yrog.apijeuxolympiques.service.OfferService;
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
    private final OfferService offerService;

    @Value("${cart.item.expiration-duration}")
    private int expirationDuration;

    public CartItemServiceImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            CartItemMapper cartItemMapper,
            OfferRepository offerRepository,
            OfferService offerService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
        this.offerRepository = offerRepository;
        this.offerService = offerService;
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
        // Vérifie si le panier existe
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        // Récupère l'offre
        Offer offer = offerRepository.findById(request.getOfferId())
                .orElseThrow(() -> new EntityNotFoundException("Offer not found"));

        // Crée un nouvel item
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setOffer(offer);
        item.setQuantity(request.getQuantity());
        item.setPriceAtPurchase(request.getPriceAtPurchase());
        item.setQrCode(UUID.randomUUID().toString());
        item.setExpirationTime(Instant.now().plus(Duration.ofMinutes(expirationDuration)));

        // Ajoute l’item à la liste du panier AVANT de sauver
        cart.getItems().add(item);
        cart.setUpdatedAt(LocalDateTime.now());

        // Calcule le nouveau montant total
        cart.setAmount(cart.getItems().stream()
                .map(i -> i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        // Sauvegarde du panier (et cascade les items)
        cartRepository.save(cart);

        // Retour DTO
        return cartItemMapper.toResponse(item);
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

    @Override
    public CartItemResponse updateItemQuantity(Long cartId, Long itemId, CartItemUpdateRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé avec l'id " + cartId));

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé avec l'id " + itemId));

        if (!cartItem.getCart().getCartId().equals(cartId)) {
            throw new RuntimeException("Cet article n'appartient pas au panier spécifié.");
        }

        int newQuantity = request.getQuantity();

        if (newQuantity <= 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }

        cartItem.setQuantity(newQuantity);
        CartItem updatedItem = cartItemRepository.save(cartItem);

        return cartItemMapper.toResponse(updatedItem);
    }

}


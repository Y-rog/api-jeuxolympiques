package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.entity.Cart;
import com.yrog.apijeuxolympiques.entity.CartItem;
import com.yrog.apijeuxolympiques.enums.CartStatus;
import com.yrog.apijeuxolympiques.mapper.CartItemMapper;
import com.yrog.apijeuxolympiques.repository.CartItemRepository;
import com.yrog.apijeuxolympiques.repository.CartRepository;
import com.yrog.apijeuxolympiques.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implémentation du service gérant les articles du panier.
 */
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final RabbitTemplate rabbitTemplate;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public List<CartItemResponse> findCartItemsByCartId(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Panier introuvable : " + cartId));
        return cartItemMapper.toResponseList(cartItemRepository.findByCart(cart));
    }

    @Override
    public void addItemToCart(Long cartId, CartItemCreateRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Panier introuvable : " + cartId));

        if (cart.getStatus() == CartStatus.PAYE) {
            throw new IllegalStateException("Le panier a déjà été payé et ne peut plus être modifié.");
        }

        Map<String, Object> message = new HashMap<>();
        message.put("action", "add");
        message.put("cartId", cartId);
        message.put("offerId", request.offerId());

        rabbitTemplate.convertAndSend("cartItemExchange", "cartitem.add", message);
    }

    @Override
    @Transactional
    public void removeItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Panier introuvable : " + cartId));

        if (cart.getStatus() == CartStatus.PAYE) {
            throw new IllegalStateException("Le panier a déjà été payé et ne peut plus être modifié.");
        }

        CartItem itemToRemove = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Article introuvable : " + cartItemId));

        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);

        cart.setAmount(cart.getItems().stream()
                .map(CartItem::getPriceAtPurchase)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Article introuvable : " + cartItemId));
    }
}



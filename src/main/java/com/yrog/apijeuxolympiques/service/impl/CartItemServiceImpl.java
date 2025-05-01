package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.enums.CartStatus;
import com.yrog.apijeuxolympiques.mapper.CartItemMapper;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import com.yrog.apijeuxolympiques.repository.CartItemRepository;
import com.yrog.apijeuxolympiques.repository.CartRepository;
import com.yrog.apijeuxolympiques.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Override
    public List<CartItemResponse> findCartItemsByCartId(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        List<CartItem> items = cartItemRepository.findByCart(cart);
        return cartItemMapper.toResponseList(items);
    }

    @Override
    public CartItemResponse addItemToCart(Long cartId, CartItemCreateRequest request) {
        Cart cart = cartRepository.findById(cartId).orElseThrow();

        if (cart.getStatus() == CartStatus.PAYE) {
            throw new IllegalStateException("Le panier a déjà été payé et ne peut plus être modifié.");
        }

        Map<String, Object> message = new HashMap<>();
        message.put("action", "add");
        message.put("cartId", cartId);
        message.put("offerId", request.getOfferId());
        message.put("priceAtPurchase", request.getPriceAtPurchase());

        rabbitTemplate.convertAndSend("cartItemExchange", "cartitem.add", message);
        return null; // À adapter si tu veux retourner une confirmation utilisateur
    }

    @Override
    @Transactional
    public void removeItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getStatus() == CartStatus.PAYE) {
            throw new IllegalStateException("Le panier a déjà été payé et ne peut plus être modifié.");
        }

        CartItem itemToRemove = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Retirer l'article du panier et supprimer
        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);

        // Recalculer le montant total du panier sans utiliser quantity
        cart.setAmount(cart.getItems().stream()
                .map(CartItem::getPriceAtPurchase)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        cartRepository.save(cart);
    }

}



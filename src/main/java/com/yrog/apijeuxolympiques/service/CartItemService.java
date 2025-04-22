package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartItemService {

    List<CartItemResponse> findCartItemsByCartId(Long cartId);

    void removeItemFromCart(Long cartId, Long cartItemId);

    @Transactional
    CartItemResponse addItemToCart(Long cartId, CartItemCreateRequest itemDto);
}

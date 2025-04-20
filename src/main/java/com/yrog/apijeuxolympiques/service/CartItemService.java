package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;

import java.util.List;

public interface CartItemService {

    List<CartItemResponse> findCartItemsByCartId(Long cartId);

    void removeItemFromCart(Long cartId, Long cartItemId);

    CartItemResponse addItemToCart(Long cartId, CartItemCreateRequest itemDto);
}

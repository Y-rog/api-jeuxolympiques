package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemService {

    List<CartItemResponse> findCartItemsByCartId(Long cartId);

    @Transactional
    void removeItemFromCart(Long cartId, Long cartItemId);

    @Transactional
    CartItemResponse addItemToCart(Long cartId, CartItemCreateRequest request);

}

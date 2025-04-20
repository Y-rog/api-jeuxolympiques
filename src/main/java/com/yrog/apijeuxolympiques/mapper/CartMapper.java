package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.cart.CartCreateRequest;
import com.yrog.apijeuxolympiques.dto.cart.CartResponse;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.pojo.CartItem;

public interface CartMapper {

    CartCreateRequest toDTO(Cart cart);

    Cart toEntity(CartCreateRequest cartDTO);

    CartResponse toResponse(Cart cart);

}

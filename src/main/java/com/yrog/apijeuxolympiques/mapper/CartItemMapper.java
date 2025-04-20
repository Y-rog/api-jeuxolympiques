package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import com.yrog.apijeuxolympiques.pojo.Offer;

import java.util.List;

public interface CartItemMapper {

    CartItemCreateRequest toDTO(CartItem cartItem);

    CartItem toEntity(CartItemCreateRequest dto);

    CartItemResponse toResponse(CartItem item);

    List<CartItemResponse> toResponseList(List<CartItem> items);

    List<CartItem> toEntityList(List<CartItemCreateRequest> requests, Cart cart, List<Offer> offers);

}


package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.cart.CartCreateRequest;
import com.yrog.apijeuxolympiques.dto.cart.CartResponse;
import com.yrog.apijeuxolympiques.entity.Cart;

public interface CartMapper {

    CartCreateRequest toDTO(Cart cart);

    Cart toEntity(CartCreateRequest cartDTO);

    CartResponse toResponse(Cart cart);

}

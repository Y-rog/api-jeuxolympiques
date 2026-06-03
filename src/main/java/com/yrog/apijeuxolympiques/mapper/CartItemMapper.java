package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper MapStruct pour convertir entre CartItem et CartItemResponse.
 */
@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "offer.offerId", target = "offerId")
    @Mapping(source = "cart.cartId", target = "cartId")
    CartItemResponse toResponse(CartItem item);

    List<CartItemResponse> toResponseList(List<CartItem> items);
}
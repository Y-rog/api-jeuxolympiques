package com.yrog.apijeuxolympiques.mapper.impl;

import com.yrog.apijeuxolympiques.dto.cart.CartCreateRequest;
import com.yrog.apijeuxolympiques.dto.cart.CartResponse;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.mapper.CartItemMapper;
import com.yrog.apijeuxolympiques.mapper.CartMapper;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapperImpl implements CartMapper {

    private final CartItemMapper cartItemMapper;

    public CartMapperImpl(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    public CartCreateRequest toDTO(Cart cart) {
        if (cart == null) return null;

        CartCreateRequest dto = new CartCreateRequest();
        dto.setStatus(cart.getStatus());
        dto.setAmount(cart.getAmount());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        dto.setTransactionUuid(cart.getTransactionUuid());
        dto.setDateTransaction(cart.getDateTransaction());
        dto.setUserId(cart.getUser() != null ? cart.getUser().getId() : null);

        if (cart.getItems() != null) {
            List<CartItemCreateRequest> itemDtos = cart.getItems().stream()
                    .map(cartItemMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setItems(itemDtos);
        }

        return dto;
    }

    @Override
    public Cart toEntity(CartCreateRequest dto) {
        if (dto == null) return null;

        Cart cart = new Cart();
        cart.setStatus(dto.getStatus());
        cart.setAmount(dto.getAmount());
        cart.setCreatedAt(dto.getCreatedAt());
        cart.setUpdatedAt(dto.getUpdatedAt());
        cart.setTransactionUuid(dto.getTransactionUuid());
        cart.setDateTransaction(dto.getDateTransaction());

        return cart;
    }

    @Override
    public CartResponse toResponse(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartResponse response = new CartResponse();
        response.setCartId(cart.getCartId());
        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());
        response.setAmount(cart.getAmount());

        // Ajoute l'id de l'utilisateur si besoin
        if (cart.getUser() != null) {
            response.setUserId(cart.getUser().getId());
        }

        return response;
    }

}




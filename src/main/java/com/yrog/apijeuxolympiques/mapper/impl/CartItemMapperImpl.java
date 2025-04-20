package com.yrog.apijeuxolympiques.mapper.impl;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemCreateRequest;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemResponse;
import com.yrog.apijeuxolympiques.mapper.CartItemMapper;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import com.yrog.apijeuxolympiques.pojo.Offer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartItemMapperImpl implements CartItemMapper {

    @Override
    public CartItemCreateRequest toDTO(CartItem cartItem) {
        if (cartItem == null) return null;

        CartItemCreateRequest dto = new CartItemCreateRequest();
        dto.setQuantity(cartItem.getQuantity());
        dto.setPriceAtPurchase(cartItem.getPriceAtPurchase());
        dto.setOfferId(cartItem.getOffer().getOfferId());

        return dto;
    }

    @Override
    public CartItem toEntity(CartItemCreateRequest dto) {
        if (dto == null) return null;

        CartItem entity = new CartItem();
        entity.setQuantity(dto.getQuantity());
        entity.setPriceAtPurchase(dto.getPriceAtPurchase());

        Offer offer = new Offer();
        offer.setOfferId(dto.getOfferId());
        entity.setOffer(offer);

        return entity;
    }

    @Override
    public CartItemResponse toResponse(CartItem item) {
        if (item == null) return null;

        CartItemResponse response = new CartItemResponse();
        response.setCartItemId(item.getCartItemId());
        response.setQuantity(item.getQuantity());
        response.setQrcode(item.getQrCode());
        response.setPriceAtPurchase(item.getPriceAtPurchase());
        response.setOfferId(item.getOffer() != null ? item.getOffer().getOfferId() : null);
        response.setAddedAt(item.getAddedAt());

        return response;
    }

    @Override
    public List<CartItemResponse> toResponseList(List<CartItem> items) {
        return items.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartItem> toEntityList(List<CartItemCreateRequest> requests, Cart cart, List<Offer> offers) {
        return requests.stream()
                .map(req -> {
                    Offer offer = offers.stream()
                            .filter(o -> o.getOfferId().equals(req.getOfferId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Offer not found for ID: " + req.getOfferId()));

                    CartItem item = toEntity(req);
                    item.setCart(cart);
                    item.setOffer(offer);
                    return item;
                })
                .collect(Collectors.toList());
    }




}



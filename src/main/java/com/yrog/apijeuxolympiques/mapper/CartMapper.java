package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.cart.CartResponse;
import com.yrog.apijeuxolympiques.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct pour convertir entre Cart et CartResponse.
 */
@Mapper(componentModel = "spring")
public interface CartMapper {

    /**
     * Convertit une entité Cart en CartResponse.
     *
     * @param cart l'entité à convertir
     * @return le DTO de réponse
     */
    @Mapping(source = "user.id", target = "userId")
    CartResponse toResponse(Cart cart);
}

package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.ticket.TicketResponse;
import com.yrog.apijeuxolympiques.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(source = "cartItemId", target = "cartItemId")
    @Mapping(source = "priceAtPurchase", target = "priceAtPurchase")
    @Mapping(source = "offer.event.eventTitle", target = "eventTitle")
    @Mapping(source = "offer.event.eventLocation", target = "eventLocation")
    @Mapping(source = "offer.event.eventDateTime", target = "eventDateTime")
    @Mapping(source = "offer.offerCategory.title", target = "offerCategoryTitle")
    @Mapping(source = "offer.offerCategory.placesPerOffer", target = "placesPerOffer")
    TicketResponse toDto(CartItem cartItem);

}









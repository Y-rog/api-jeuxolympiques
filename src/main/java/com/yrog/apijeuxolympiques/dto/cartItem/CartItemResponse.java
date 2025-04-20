package com.yrog.apijeuxolympiques.dto.cartItem;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CartItemResponse {

    private Long cartItemId;
    private Integer quantity;
    private String qrcode;
    private BigDecimal priceAtPurchase;
    private Long offerId;
    private Long cartId;
    private LocalDateTime addedAt;

}

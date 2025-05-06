package com.yrog.apijeuxolympiques.dto.cartItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemQRCodeDTO {
    private Long cartItemId;
    private String qrCode;
}


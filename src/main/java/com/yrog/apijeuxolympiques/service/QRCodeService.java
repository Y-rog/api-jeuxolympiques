package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemQRCodeDTO;
import com.yrog.apijeuxolympiques.pojo.Cart;

import java.util.List;

public interface QRCodeService {
    byte[] generateQRCode(String text, int width, int height) throws Exception;

    List<CartItemQRCodeDTO> generateQRCodesForCart(Cart cart, int width, int height);
}

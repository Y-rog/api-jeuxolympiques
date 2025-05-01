package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.cart.CartCreateRequest;
import com.yrog.apijeuxolympiques.dto.cart.CartResponse;
import com.yrog.apijeuxolympiques.pojo.Cart;


public interface CartService {
    CartResponse createCart(CartCreateRequest request);
    CartResponse getCartById(Long id);
    void deleteCart(Long id);
    CartResponse findCartByUser();
    void updateCartAmount(Cart cart);

    void confirmPaymentAndGenerateQRCode(Long cartId, boolean simulateFailure);

    Cart simulateFailedPayment(Long cartId);

    Cart validatePayment(Long cartId);

    void generateQRCodeCartItems(Cart cart);
}


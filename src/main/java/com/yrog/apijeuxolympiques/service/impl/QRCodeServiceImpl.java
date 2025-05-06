package com.yrog.apijeuxolympiques.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemQRCodeDTO;
import com.yrog.apijeuxolympiques.pojo.Cart;
import com.yrog.apijeuxolympiques.service.QRCodeService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QRCodeServiceImpl implements QRCodeService {

    @Override
    public byte[] generateQRCode(String text, int width, int height) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public List<CartItemQRCodeDTO> generateQRCodesForCart(Cart cart, int width, int height) {
        String transactionUuid = cart.getTransactionUuid();
        String userKey = cart.getUser().getSecretKey();
        if (transactionUuid == null || userKey == null) {
            throw new IllegalStateException("Cart or user not ready for QR generation");
        }
        return cart.getItems().stream().map(item -> {
            String qrCodeKey = transactionUuid + "_" + userKey;
            byte[] qrCodePng = null;
            try {
                qrCodePng = generateQRCode(qrCodeKey, width, height);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String b64  = Base64.getEncoder().encodeToString(qrCodePng);
            CartItemQRCodeDTO dto = new CartItemQRCodeDTO();
            dto.setCartItemId(item.getCartItemId());
            dto.setQrCode(b64);
            return dto;
        }).collect(Collectors.toList());
    }
}


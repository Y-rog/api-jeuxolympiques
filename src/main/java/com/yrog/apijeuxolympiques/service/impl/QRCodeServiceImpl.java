package com.yrog.apijeuxolympiques.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.yrog.apijeuxolympiques.dto.cartItem.CartItemQRCodeDTO;
import com.yrog.apijeuxolympiques.entity.Cart;
import com.yrog.apijeuxolympiques.service.QRCodeService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

/**
 * Implémentation du service de génération de QR codes.
 */
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
            throw new IllegalStateException("Panier ou utilisateur non prêt pour la génération de QR codes.");
        }

        String qrCodeKey = transactionUuid + "_" + userKey;

        return cart.getItems().stream()
                .map(item -> {
                    try {
                        String b64 = Base64.getEncoder().encodeToString(
                                generateQRCode(qrCodeKey, width, height)
                        );
                        return new CartItemQRCodeDTO(item.getCartItemId(), b64);
                    } catch (Exception e) {
                        throw new RuntimeException("Erreur lors de la génération du QR code", e);
                    }
                })
                .toList();
    }
}


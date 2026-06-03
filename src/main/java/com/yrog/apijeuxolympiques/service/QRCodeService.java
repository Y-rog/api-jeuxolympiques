package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.cartItem.CartItemQRCodeDTO;
import com.yrog.apijeuxolympiques.entity.Cart;
import java.util.List;

/**
 * Service gérant la génération de QR codes pour les billets.
 */
public interface QRCodeService {

    /**
     * Génère un QR code en PNG pour un texte donné.
     *
     * @param text   le texte à encoder
     * @param width  la largeur du QR code en pixels
     * @param height la hauteur du QR code en pixels
     * @return le QR code en bytes
     */
    byte[] generateQRCode(String text, int width, int height) throws Exception;

    /**
     * Génère les QR codes pour tous les articles d'un panier.
     *
     * @param cart   le panier dont on génère les QR codes
     * @param width  la largeur du QR code en pixels
     * @param height la hauteur du QR code en pixels
     * @return la liste des QR codes générés
     */
    List<CartItemQRCodeDTO> generateQRCodesForCart(Cart cart, int width, int height);
}

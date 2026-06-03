package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.ticket.TicketResponse;
import com.yrog.apijeuxolympiques.entity.CartItem;
import com.yrog.apijeuxolympiques.enums.CartStatus;
import com.yrog.apijeuxolympiques.mapper.TicketMapper;
import com.yrog.apijeuxolympiques.repository.CartItemRepository;
import com.yrog.apijeuxolympiques.service.QRCodeService;
import com.yrog.apijeuxolympiques.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

/**
 * Implémentation du service gérant la génération des tickets.
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketMapper ticketMapper;
    private final QRCodeService qrCodeService;
    private final CartItemRepository cartItemRepository;

    @Override
    public TicketResponse generateTicket(CartItem cartItem) {
        String qrCodeKey = cartItem.getCart().getTransactionUuid() + "_"
                + cartItem.getCart().getUser().getSecretKey();
        try {
            String base64QrCode = "data:image/png;base64," + Base64.getEncoder()
                    .encodeToString(qrCodeService.generateQRCode(qrCodeKey, 200, 200));

            return new TicketResponse(
                    cartItem.getCartItemId(),
                    base64QrCode,
                    cartItem.getPriceAtPurchase(),
                    cartItem.getOffer().getEvent().getEventTitle(),
                    cartItem.getOffer().getEvent().getEventLocation(),
                    cartItem.getOffer().getEvent().getEventDateTime(),
                    cartItem.getOffer().getOfferCategory().getTitle(),
                    cartItem.getOffer().getOfferCategory().getPlacesPerOffer()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du QR code", e);
        }
    }

    @Override
    public List<TicketResponse> generateTicketsByUser(Long userId) {
        return cartItemRepository.findByCartUserIdAndCartStatus(userId, CartStatus.PAYE)
                .stream()
                .map(this::generateTicket)
                .toList();
    }
}


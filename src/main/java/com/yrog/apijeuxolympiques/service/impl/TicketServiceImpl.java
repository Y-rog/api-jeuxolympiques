package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.ticket.TicketResponse;
import com.yrog.apijeuxolympiques.enums.CartStatus;
import com.yrog.apijeuxolympiques.mapper.TicketMapper;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import com.yrog.apijeuxolympiques.repository.CartItemRepository;
import com.yrog.apijeuxolympiques.security.repository.UserRepository;
import com.yrog.apijeuxolympiques.service.QRCodeService;
import com.yrog.apijeuxolympiques.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public TicketResponse generateTicket(CartItem cartItem) {
        TicketResponse ticketResponse = ticketMapper.toDto(cartItem);
        String qrCodeKey = cartItem.getCart().getTransactionUuid() + "_" + cartItem.getCart().getUser().getSecretKey();
        try {
            byte[] qrCodeBytes = qrCodeService.generateQRCode(qrCodeKey, 200, 200);
            String base64QrCode = "data:image/png;base64," + Base64.getEncoder().encodeToString(qrCodeBytes);
            ticketResponse.setQrCode(base64QrCode);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du QR code", e);
        }
        return ticketResponse;
    }


    @Override
    public List<TicketResponse> generateTicketsByUser(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByCartUserIdAndCartStatus(userId, CartStatus.PAYE);
        return cartItems.stream()
                .map(this::generateTicket)
                .collect(Collectors.toList());
    }



}


package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.ticket.TicketResponse;
import com.yrog.apijeuxolympiques.entity.CartItem;
import com.yrog.apijeuxolympiques.security.service.UserDetailsImpl;
import com.yrog.apijeuxolympiques.service.CartItemService;
import com.yrog.apijeuxolympiques.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller gérant les tickets de billetterie.
 */
@RestController
@RequestMapping("api-jeuxolympiques/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final CartItemService cartItemService;

    /**
     * Récupère un ticket par l'identifiant de l'article du panier.
     */
    @GetMapping("/{cartItemId}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long cartItemId) {
        CartItem cartItem = cartItemService.getCartItem(cartItemId);
        return ResponseEntity.ok(ticketService.generateTicket(cartItem));
    }

    /**
     * Récupère tous les tickets de l'utilisateur connecté.
     */
    @GetMapping("/me")
    public ResponseEntity<List<TicketResponse>> getTicketsByUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(ticketService.generateTicketsByUser(userDetails.getId()));
    }
}



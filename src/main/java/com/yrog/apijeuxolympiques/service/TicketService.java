package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.ticket.TicketResponse;
import com.yrog.apijeuxolympiques.entity.CartItem;
import java.util.List;

/**
 * Service gérant la génération des tickets de billetterie.
 */
public interface TicketService {

    /**
     * Génère un ticket avec QR code pour un article du panier.
     */
    TicketResponse generateTicket(CartItem cartItem);

    /**
     * Génère tous les tickets d'un utilisateur pour ses paniers payés.
     */
    List<TicketResponse> generateTicketsByUser(Long userId);
}

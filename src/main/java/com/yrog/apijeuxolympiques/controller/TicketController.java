package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.ticket.TicketResponse;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import com.yrog.apijeuxolympiques.service.CartItemService;
import com.yrog.apijeuxolympiques.service.CartService;
import com.yrog.apijeuxolympiques.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-jeuxolympiques/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/{cartItemId}")
    public TicketResponse getTicket(@PathVariable Long cartItemId) {
        CartItem cartItem = cartItemService.getCartItem(cartItemId);
        return ticketService.generateTicket(cartItem);
    }
}



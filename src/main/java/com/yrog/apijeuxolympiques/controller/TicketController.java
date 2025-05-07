package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.ticket.TicketResponse;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import com.yrog.apijeuxolympiques.security.models.User;
import com.yrog.apijeuxolympiques.security.service.UserDetailsImpl;
import com.yrog.apijeuxolympiques.service.CartItemService;
import com.yrog.apijeuxolympiques.service.CartService;
import com.yrog.apijeuxolympiques.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/me")
    public List<TicketResponse> getTicketsByUser(Authentication authentication) {
        UserDetailsImpl userDetails  = (UserDetailsImpl) authentication.getPrincipal();
        return ticketService.generateTicketsByUser(userDetails .getId());
    }


}



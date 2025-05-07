package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.ticket.TicketResponse;
import com.yrog.apijeuxolympiques.pojo.CartItem;

import java.util.List;

public interface TicketService {

    TicketResponse generateTicket(CartItem cartItem);

    List<TicketResponse> generateTicketsByUser(Long userId);
}

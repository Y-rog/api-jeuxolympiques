package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.ticket.TicketResponse;
import com.yrog.apijeuxolympiques.pojo.CartItem;

public interface TicketService {


    TicketResponse generateTicket(CartItem cartItem);
}

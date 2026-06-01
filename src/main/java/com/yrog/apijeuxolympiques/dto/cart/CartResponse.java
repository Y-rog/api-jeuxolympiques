package com.yrog.apijeuxolympiques.dto.cart;

import com.yrog.apijeuxolympiques.enums.CartStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CartResponse {
    private Long cartId;
    private CartStatus status;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String transactionUuid;
    private LocalDateTime dateTransaction;
    private Long userId;

}
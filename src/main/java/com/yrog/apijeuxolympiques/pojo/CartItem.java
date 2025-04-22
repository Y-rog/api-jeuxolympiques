package com.yrog.apijeuxolympiques.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    private Integer quantity;

    private String qrCode;

    private BigDecimal priceAtPurchase;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    private LocalDateTime addedAt;

    private Instant expirationTime;

    @PrePersist
    protected void onAdd() {
        addedAt = LocalDateTime.now();
    }

    public BigDecimal getItemTotal() {
        return priceAtPurchase.multiply(BigDecimal.valueOf(quantity));
    }

    public CartItem() {}

}

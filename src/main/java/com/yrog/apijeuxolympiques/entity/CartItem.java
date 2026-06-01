package com.yrog.apijeuxolympiques.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Entité représentant un article dans un panier.
 * Un article est lié à une offre et expire après un certain délai.
 */
@Entity
@Getter
@Setter
@Table(name="Cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @Column(nullable = false)
    private String qrCode;

    @Column(nullable = false, precision = 10, scale =2)
    private BigDecimal priceAtPurchase;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "offer_id", nullable = false)
    private Offer offer;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    @Column(nullable = false)
    private Instant expirationTime;

    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }

}

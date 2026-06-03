package com.yrog.apijeuxolympiques.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entité représentant une offre de billetterie pour un événement olympique.
 * Une offre est liée à un événement et une catégorie (Solo, Duo, Familiale).
 */
@Entity
@Getter
@Setter
@Table (name="offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="offer_id")
    private Long offerId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean availability;

    @ManyToOne
    @JoinColumn(name = "offer_category_id", nullable = false)
    private OfferCategory offerCategory;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "is_active", nullable = false)
    private boolean active = false;

}


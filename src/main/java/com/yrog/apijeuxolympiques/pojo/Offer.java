package com.yrog.apijeuxolympiques.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;  // Identifiant unique de l'offre

    private BigDecimal price;
    private boolean availability;

    @ManyToOne
    @JoinColumn(name = "offer_category_id")
    private OfferCategory offerCategory;  // Relation avec la catégorie d'offre

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;  // Relation avec l'événement

    private boolean isActive = false;

    public Offer() {}

}


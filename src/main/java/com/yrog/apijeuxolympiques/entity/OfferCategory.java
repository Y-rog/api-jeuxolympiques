package com.yrog.apijeuxolympiques.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité représentant une catégorie d'offre (Solo, Duo, Familiale).
 * Définit le nombre de places incluses par offre.
 */
@Entity
@Getter
@Setter
@Table(name = "offer_categories")
public class OfferCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, length = 50)
    private String title;

    /**
     * Nombre de places incluses par offre pour cette catégorie.
     */
    @Column(nullable = false)
    private Integer placesPerOffer;
}


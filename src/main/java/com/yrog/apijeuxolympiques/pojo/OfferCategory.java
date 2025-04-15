package com.yrog.apijeuxolympiques.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OfferCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;  // Identifiant de la catégorie

    private String title;  // Titre de la catégorie (ex : "Duo", "Solo", etc.)
    private Integer placesPerOffer;  // Nombre de places par offre

    public OfferCategory(String title, Integer placesPerOffer) {
        this.title = title;
        this.placesPerOffer = placesPerOffer;
    }

    public OfferCategory() {}

}


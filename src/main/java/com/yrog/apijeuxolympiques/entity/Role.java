package com.yrog.apijeuxolympiques.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité représentant un rôle utilisateur.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

    /**
     * Identifiant unique du rôle, généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    /**
     * Nom du rôle (ADMIN ou USER).
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }

    public Role() {}
}

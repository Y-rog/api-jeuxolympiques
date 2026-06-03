package com.yrog.apijeuxolympiques.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un utilisateur de l'application.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Getter
@Setter
public class User {

    /**
     * Identifiant unique de l'utilisateur, généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * Prénom de l'utilisateur.
     */
    @Column(nullable = false, length = 50)
    private String firstname;

    /**
     * Nom de l'utilisateur.
     */
    @Column(nullable = false, length = 50)
    private String lastname;

    /**
     * Email de l'utilisateur — utilisé comme identifiant de connexion.
     */
    @Column(nullable = false, length = 50, unique = true)
    private String username;

    /**
     * Mot de passe hashé avec BCrypt.
     */
    @Column(nullable = false, length = 120)
    private String password;

    /**
     * Clé secrète unique générée à la création, utilisée pour sécuriser les QR codes.
     */
    @Column(name = "secret_key", unique = true, nullable = false)
    private String secretKey;

    /**
     * Rôles assignés à l'utilisateur.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String firstname, String lastname, String username, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
    }

    public User() {}
}

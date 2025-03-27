package com.yrog.apijeuxolympiques.security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table( name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max=50)
    private String firstname;

    @NotBlank
    @Size(max=50)
    private String lastname;

    @NotBlank
    @Size(max=50)
    @Email
    private String username;

    @NotBlank
    @Size(max=120)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un caractère spécial")
    private String password;

    // Clé générée pour l'utilisateur, uniquement visible par l'organisation
    @Column(name = "secret_key", unique = true, nullable = false)
    private String secretKey;

    @ManyToMany(fetch = FetchType.EAGER )
    @JoinTable(name="user_roles",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String firstname, String lastname, String username, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.secretKey = UUID.randomUUID().toString();

        Role userRole = new Role(ERole.USER);
        this.roles.add(userRole);
    }

    public User() {

    }
}

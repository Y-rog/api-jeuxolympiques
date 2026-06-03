package com.yrog.apijeuxolympiques.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * DTO représentant la requête d'inscription.
 *
 * @param firstname le prénom de l'utilisateur
 * @param lastname  le nom de l'utilisateur
 * @param username  l'email de l'utilisateur
 * @param password  le mot de passe de l'utilisateur
 */
public record SignupRequest(

        @Schema(description = "Prénom", example = "Jean")
        @NotBlank(message = "Le prénom est obligatoire")
        @Size(min = 2, max = 100)
        String firstname,

        @Schema(description = "Nom", example = "Dupont")
        @NotBlank(message = "Le nom est obligatoire")
        @Size(min = 2, max = 100)
        String lastname,

        @Schema(description = "Email", example = "jeandupont@mail.com")
        @NotBlank(message = "Le mail est obligatoire")
        @Email(message = "L'email doit être valide")
        @Size(min = 5, max = 100)
        String username,

        @Schema(description = "Mot de passe", example = "jeanDupont123@")
        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 8, max = 40)
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?=\\S+$).{8,}$",
                message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un caractère spécial")
        String password

) {}

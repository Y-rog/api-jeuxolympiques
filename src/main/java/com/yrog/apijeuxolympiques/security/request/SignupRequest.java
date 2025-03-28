package com.yrog.apijeuxolympiques.security.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class SignupRequest {

    @Schema(description = "Prénom", example = "Jean")
    @Size(min = 2, max = 100, message = "Le prénom doit avoir entre 2 et 100 caractères")
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstname;

    @Schema(description = "Nom", example = "Dupont")
    @Size(min = 2, max = 100, message = "Le nom doit avoir entre 2 et 100 caractères")
    @NotBlank(message = "Le nom est obligatoire")
    private String lastname;

    @Schema(description = "Email", example = "jeandupont@mail.com")
    @Size(min = 5, max = 100, message = "Le mail doit avoir entre 5 et 100 caractères")
    @NotBlank(message = "Le mail est obligatoire")
    @Email(message = "L'email doit être valide")
    private String username;

    @Schema(description = "Mot de passe", example = "jeanDupont123@")
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size (min = 8, max = 40)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un caractère spécial")
    private String password;

}

package com.yrog.apijeuxolympiques.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Schema(description = "Email", example = "jeandupont@mail.com")
    @NotBlank(message = "Le mail est obligatoire")
    @Email(message = "L'email doit être valide")
    private String username;

    @Schema(description = "Mot de passe", example = "jeanDupont123@")
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}

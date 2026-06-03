package com.yrog.apijeuxolympiques.dto.auth;

import java.util.List;

/**
 * DTO représentant la réponse JWT après authentification.
 *
 * @param token    le token JWT généré
 * @param username l'email de l'utilisateur connecté
 * @param roles    la liste des rôles de l'utilisateur
 */
public record JwtResponse(
        String token,
        String username,
        List<String> roles
) {}


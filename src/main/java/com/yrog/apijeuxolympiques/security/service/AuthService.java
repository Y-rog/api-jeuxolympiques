package com.yrog.apijeuxolympiques.security.service;

import com.yrog.apijeuxolympiques.dto.auth.SignupRequest;
import com.yrog.apijeuxolympiques.entity.User;
import com.yrog.apijeuxolympiques.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service gérant les opérations d'authentification.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Change le mot de passe d'un utilisateur.
     *
     * @param request la requête contenant le username et le nouveau mot de passe
     */
    public void changePassword(SignupRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable : " + request.username()));

        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }
}

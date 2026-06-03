package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.auth.JwtResponse;
import com.yrog.apijeuxolympiques.dto.auth.LoginRequest;
import com.yrog.apijeuxolympiques.dto.auth.MessageResponse;
import com.yrog.apijeuxolympiques.dto.auth.SignupRequest;
import com.yrog.apijeuxolympiques.entity.ERole;
import com.yrog.apijeuxolympiques.entity.Role;
import com.yrog.apijeuxolympiques.entity.User;
import com.yrog.apijeuxolympiques.repository.RoleRepository;
import com.yrog.apijeuxolympiques.repository.UserRepository;
import com.yrog.apijeuxolympiques.security.jwt.JwtUtils;
import com.yrog.apijeuxolympiques.security.service.AuthService;
import com.yrog.apijeuxolympiques.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Controller gérant l'authentification et l'inscription des utilisateurs.
 */
@RestController
@RequestMapping("api-jeuxolympiques/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    /**
     * Authentifie un utilisateur et retourne un token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        String username = jwtUtils.getUsernameFromJwtToken(jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new JwtResponse(jwt, username, roles));
    }

    /**
     * Change le mot de passe d'un utilisateur.
     */
    @PutMapping("/changePassword")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody SignupRequest signupRequest) {
        authService.changePassword(signupRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * Inscrit un nouvel utilisateur.
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.username())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Cet email est déjà utilisé."));
        }

        User user = new User(
                signupRequest.firstname(),
                signupRequest.lastname(),
                signupRequest.username(),
                passwordEncoder.encode(signupRequest.password())
        );

        user.setSecretKey(UUID.randomUUID().toString());

        Role userRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new RuntimeException("Rôle USER introuvable."));

        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Utilisateur créé avec succès."));
    }
}

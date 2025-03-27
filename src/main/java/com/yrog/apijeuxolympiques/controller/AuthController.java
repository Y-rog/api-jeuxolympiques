package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.security.jwt.JwtUtils;
import com.yrog.apijeuxolympiques.security.models.ERole;
import com.yrog.apijeuxolympiques.security.models.Role;
import com.yrog.apijeuxolympiques.security.models.User;
import com.yrog.apijeuxolympiques.security.repository.RoleRepository;
import com.yrog.apijeuxolympiques.security.repository.UserRepository;
import com.yrog.apijeuxolympiques.security.request.LoginRequest;
import com.yrog.apijeuxolympiques.security.request.SignupRequest;
import com.yrog.apijeuxolympiques.security.response.JwtResponse;
import com.yrog.apijeuxolympiques.security.response.MessageResponse;
import com.yrog.apijeuxolympiques.security.service.AuthService;
import com.yrog.apijeuxolympiques.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api-jeuxolympiques/auth")
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));

    }

    @PutMapping("changePassword")
    public void changePassword (@Valid @RequestBody SignupRequest signupRequest) {
        this.authService.changePassword(signupRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        // Vérifie si l'email existe déjà
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Crée un nouvel utilisateur avec le nom, prénom, email, et mot de passe encodé
        User user = new User(signupRequest.getFirstname(), signupRequest.getLastname(), signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()));

        // Génère une clé secrète unique pour l'utilisateur
        user.setSecretKey(UUID.randomUUID().toString());

        // Définit le rôle par défaut en tant que "USER"
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new RuntimeException("Error: Role 'USER' is not found."));
        roles.add(userRole);

        // Assigne les rôles à l'utilisateur
        user.setRoles(roles);

        // Sauvegarde l'utilisateur dans la base de données
        userRepository.save(user);

        // Retourne une réponse de succès
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }


}

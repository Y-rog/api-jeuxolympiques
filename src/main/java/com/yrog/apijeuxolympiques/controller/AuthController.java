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
import java.util.stream.Collectors;

@RestController
@RequestMapping("auth")
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

    @PostMapping("signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser (@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Username already exists");
        }

        User user = new User(signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles != null) {
            Role userRole = roleRepository.findByName(ERole.EMPLOYEE)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;

                        default:
                            Role userRole = roleRepository.findByName(ERole.CUSTOMER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                            break;

                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));

    }

}

package com.yrog.apijeuxolympiques;

import com.yrog.apijeuxolympiques.controller.AuthController;
import com.yrog.apijeuxolympiques.dto.auth.JwtResponse;
import com.yrog.apijeuxolympiques.dto.auth.LoginRequest;
import com.yrog.apijeuxolympiques.entity.ERole;
import com.yrog.apijeuxolympiques.entity.Role;
import com.yrog.apijeuxolympiques.repository.RoleRepository;
import com.yrog.apijeuxolympiques.repository.UserRepository;
import com.yrog.apijeuxolympiques.security.jwt.JwtUtils;
import com.yrog.apijeuxolympiques.security.service.AuthService;
import com.yrog.apijeuxolympiques.security.service.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerUnitTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthService authService;

    @Mock
    private Authentication authentication;

    private UserDetailsImpl userDetails;
    private final LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetails = new UserDetailsImpl(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void authenticateUser_shouldReturnJwtResponse() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("fake-jwt-token");
        when(jwtUtils.getUsernameFromJwtToken("fake-jwt-token")).thenReturn("john.doe@example.com");

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(JwtResponse.class, response.getBody());

        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("fake-jwt-token", jwtResponse.token());
        assertEquals("john.doe@example.com", jwtResponse.username());
        assertEquals(List.of("ROLE_USER"), jwtResponse.roles());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(authentication);
        verify(jwtUtils).getUsernameFromJwtToken("fake-jwt-token");
    }

    @Test
    void registerUser_shouldReturnCreated_whenEmailNotTaken() {
        when(userRepository.existsByUsername("john.doe@example.com")).thenReturn(false);
        when(roleRepository.findByName(ERole.USER)).thenReturn(Optional.of(new Role(ERole.USER)));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        var signupRequest = new com.yrog.apijeuxolympiques.dto.auth.SignupRequest(
                "John", "Doe", "john.doe@example.com", "Password123@"
        );

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenEmailAlreadyTaken() {
        when(userRepository.existsByUsername("john.doe@example.com")).thenReturn(true);

        var signupRequest = new com.yrog.apijeuxolympiques.dto.auth.SignupRequest(
                "John", "Doe", "john.doe@example.com", "Password123@"
        );

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertEquals(400, response.getStatusCode().value());
    }
}

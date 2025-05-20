package com.yrog.apijeuxolympiques;

import com.yrog.apijeuxolympiques.controller.AuthController;
import com.yrog.apijeuxolympiques.security.jwt.JwtUtils;
import com.yrog.apijeuxolympiques.security.request.LoginRequest;
import com.yrog.apijeuxolympiques.security.response.JwtResponse;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthControllerUnitTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private LoginRequest loginRequest;

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Création d'un UserDetailsImpl factice avec des rôles
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
        // Préparer le LoginRequest
        loginRequest.setUsername("john.doe@example.com");
        loginRequest.setPassword("password");

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(jwtUtils.generateJwtToken(authentication)).thenReturn("fake-jwt-token");

        when(jwtUtils.getUsernameFromJwtToken("fake-jwt-token")).thenReturn("john.doe@example.com");

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(JwtResponse.class, response.getBody());

        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("fake-jwt-token", jwtResponse.getToken());
        assertEquals("john.doe@example.com", jwtResponse.getUsername());
        assertEquals(List.of("ROLE_USER"), jwtResponse.getRoles());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(authentication);
        verify(jwtUtils).getUsernameFromJwtToken("fake-jwt-token");
    }
}

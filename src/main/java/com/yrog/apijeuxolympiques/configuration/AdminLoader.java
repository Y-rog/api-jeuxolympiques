package com.yrog.apijeuxolympiques.configuration;

import com.yrog.apijeuxolympiques.entity.ERole;
import com.yrog.apijeuxolympiques.entity.Role;
import com.yrog.apijeuxolympiques.entity.User;
import com.yrog.apijeuxolympiques.repository.RoleRepository;
import com.yrog.apijeuxolympiques.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

/**
 * Chargeur de données initiales au démarrage de l'application.
 * Crée les rôles et le compte administrateur s'ils n'existent pas.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminLoader {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.firstname}")
    private String adminFirstName;

    @Value("${admin.lastname}")
    private String adminLastName;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    /**
     * Initialise les rôles et le compte administrateur au démarrage.
     */
    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(ERole.ADMIN));
                roleRepository.save(new Role(ERole.USER));
                log.info("Rôles créés avec succès.");
            }

            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User adminUser = new User(
                        adminFirstName,
                        adminLastName,
                        adminUsername,
                        passwordEncoder.encode(adminPassword)
                );

                Role adminRole = roleRepository.findByName(ERole.ADMIN)
                        .orElseThrow(() -> new RuntimeException("Rôle ADMIN introuvable."));

                adminUser.setRoles(Set.of(adminRole));
                userRepository.save(adminUser);
                log.info("Compte administrateur créé avec succès.");
            }
        };
    }
}



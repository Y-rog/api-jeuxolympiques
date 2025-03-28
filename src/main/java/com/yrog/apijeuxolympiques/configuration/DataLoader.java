package com.yrog.apijeuxolympiques.configuration;

import com.yrog.apijeuxolympiques.security.models.ERole;
import com.yrog.apijeuxolympiques.security.models.Role;
import com.yrog.apijeuxolympiques.security.models.User;
import com.yrog.apijeuxolympiques.security.repository.RoleRepository;
import com.yrog.apijeuxolympiques.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataLoader {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${admin.firstname}")
    private String adminFirstName;

    @Value("${admin.lastname}")
    private String adminLastName;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner loadData() {

        return args -> {
            // Charger les rôles si la table est vide
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(ERole.ADMIN));
                roleRepository.save(new Role(ERole.USER));
            }

            // Vérifier si l'utilisateur 'admin' existe
            try {
                userDetailsService.loadUserByUsername("admin");  // Essaie de charger l'utilisateur 'admin'

            } catch (UsernameNotFoundException e) {
                // Si l'utilisateur 'admin' n'existe pas, le créer
                User adminUser = new User( adminFirstName,  adminLastName,  adminUsername, passwordEncoder.encode( adminPassword));

                // Ajouter le rôle admin
                Role adminRole = roleRepository.findByName(ERole.ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role 'ADMIN' not found."));

                adminUser.setRoles(Set.of(adminRole));

                // Sauvegarder l'utilisateur dans la base de données
                userRepository.save(adminUser);

                System.out.println("Admin user created with username: admin");
            }
        };
    }
}



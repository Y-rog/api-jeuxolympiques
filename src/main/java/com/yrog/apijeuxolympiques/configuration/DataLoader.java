package com.yrog.apijeuxolympiques.configuration;

import com.yrog.apijeuxolympiques.security.models.ERole;
import com.yrog.apijeuxolympiques.security.models.Role;
import com.yrog.apijeuxolympiques.security.models.User;
import com.yrog.apijeuxolympiques.security.repository.RoleRepository;
import com.yrog.apijeuxolympiques.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
                User adminUser = new User( "admin", "admin", "admin@admin.com", passwordEncoder.encode("admin123@"));

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



package com.yrog.apijeuxolympiques.configuration;

import com.yrog.apijeuxolympiques.pojo.Event;
import com.yrog.apijeuxolympiques.pojo.Offer;
import com.yrog.apijeuxolympiques.pojo.OfferCategory;
import com.yrog.apijeuxolympiques.repository.EventRepository;
import com.yrog.apijeuxolympiques.repository.OfferCategoryRepository;
import com.yrog.apijeuxolympiques.repository.OfferRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Configuration
public class DataLoader {

    @Autowired
    private OfferCategoryRepository offerCategoryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private OfferRepository offerRepository;

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
                userDetailsService.loadUserByUsername(adminUsername);  // Essaie de charger l'utilisateur 'admin'

            } catch (UsernameNotFoundException e) {
                // Si l'utilisateur 'admin' n'existe pas, le créer
                User adminUser = new User( adminFirstName,  adminLastName,  adminUsername, passwordEncoder.encode( adminPassword));

                // Ajouter le rôle admin
                Role adminRole = roleRepository.findByName(ERole.ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role 'ADMIN' not found."));

                adminUser.setRoles(Set.of(adminRole));

                // Sauvegarder l'utilisateur dans la base de données
                userRepository.save(adminUser);
            }

            // === CATEGORIES D’OFFRES ===
            if (offerCategoryRepository.count() == 0) {
                OfferCategory solo = new OfferCategory("Solo", 1);
                OfferCategory duo = new OfferCategory("Duo", 2);
                OfferCategory familiale = new OfferCategory("Familiale", 4);
                offerCategoryRepository.saveAll(List.of(solo, duo, familiale));
            }

            // === EVENEMENTS ===
            if (eventRepository.count() == 0) {
                List<Event> events = List.of(
                        new Event("Finale Football", "Match pour la médaille d'or", "Stade de France", 80000, LocalDateTime.of(2024, 7, 30, 20, 0)),
                        new Event("Demi-finale Basketball", "Les meilleurs s'affrontent", "Stade de Lille", 15000, LocalDateTime.of(2024, 7, 28, 18, 0)),
                        new Event("Finale Natation", "Courses finales", "Centre Aquatique", 5000, LocalDateTime.of(2024, 7, 25, 19, 0)),
                        new Event("Escrime par équipe", "Phase finale", "Grand Palais", 3000, LocalDateTime.of(2024, 7, 26, 17, 0)),
                        new Event("Gymnastique", "Compétition finale", "Bercy Arena", 10000, LocalDateTime.of(2024, 7, 24, 16, 0)),
                        new Event("Volley Ball", "Quart de finale", "Stade Pierre Mauroy", 12000, LocalDateTime.of(2024, 7, 23, 20, 0)),
                        new Event("Handball", "Demi-finale femmes", "Stade de Lyon", 14000, LocalDateTime.of(2024, 7, 27, 18, 30)),
                        new Event("Athlétisme 100m", "Grande finale du 100m", "Stade de France", 60000, LocalDateTime.of(2024, 7, 29, 21, 0)),
                        new Event("Tennis", "Finale simple hommes", "Roland Garros", 15000, LocalDateTime.of(2024, 7, 31, 15, 0)),
                        new Event("Boxe", "Finale poids lourds", "Zénith de Paris", 7000, LocalDateTime.of(2024, 8, 1, 20, 30))
                );
                eventRepository.saveAll(events);
            }

            // === OFFRES ===
            if (offerRepository.count() == 0) {
                List<OfferCategory> categories = offerCategoryRepository.findAll();
                List<Event> events = eventRepository.findAll();
                Random random = new Random();

                List<Offer> offers = new ArrayList<>();

                for (int i = 1; i <= 20; i++) {
                    Offer offer = new Offer();

                    // Choisir un événement et une catégorie aléatoirement
                    Event event = events.get(random.nextInt(events.size()));
                    OfferCategory category = categories.get(random.nextInt(categories.size()));

                    // Logique de prix en fonction du nombre de places
                    double basePricePerSeat = 15 + random.nextInt(10); // entre 15€ et 25€ par place
                    double calculatedPrice = basePricePerSeat * category.getPlacesPerOffer();

                    offer.setAvailability(true); // entre 30 et 100
                    offer.setPrice(calculatedPrice);
                    offer.setEvent(event);
                    offer.setOfferCategory(category);

                    offers.add(offer);
                }

                offerRepository.saveAll(offers);
            }
        };
    }
}



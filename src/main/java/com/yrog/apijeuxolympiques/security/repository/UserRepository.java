package com.yrog.apijeuxolympiques.security.repository;

import com.yrog.apijeuxolympiques.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Recherche d'un utilisateur par son username
    Optional<User> findByUsername(String username);

    // Recherche des utilisateurs par leur rôle
    @Query(value = "SELECT u.* FROM users u " +
            "JOIN user_roles ur ON ur.user_id = u.id " +
            "JOIN roles r ON r.id = ur.role_id " +
            "WHERE r.name = ?1",
            nativeQuery = true)
    List<User> findByRoles(String role);

    // Vérifie si un utilisateur avec ce username existe déjà
    boolean existsByUsername(String username);

}

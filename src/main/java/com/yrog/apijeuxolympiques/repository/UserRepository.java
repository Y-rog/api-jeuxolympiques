package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository gérant les opérations sur les utilisateurs.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par son email.
     */
    Optional<User> findByUsername(String username);

    /**
     * Vérifie si un utilisateur avec cet email existe déjà.
     */
    boolean existsByUsername(String username);

    /**
     * Recherche les utilisateurs par leur rôle.
     */
    @Query(value = "SELECT u.* FROM users u " +
            "JOIN user_roles ur ON ur.user_id = u.id " +
            "JOIN roles r ON r.id = ur.role_id " +
            "WHERE r.name = ?1",
            nativeQuery = true)
    List<User> findByRoles(String role);
}

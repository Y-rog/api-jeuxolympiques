package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.entity.ERole;
import com.yrog.apijeuxolympiques.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository gérant les opérations sur les rôles utilisateurs.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Recherche un rôle par son nom.
     */
    Optional<Role> findByName(ERole name);
}

package com.yrog.apijeuxolympiques.security.repository;

import com.yrog.apijeuxolympiques.security.models.ERole;
import com.yrog.apijeuxolympiques.security.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role>findByName(ERole name);
}

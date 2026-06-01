package com.yrog.apijeuxolympiques.repository;

import com.yrog.apijeuxolympiques.entity.ERole;
import com.yrog.apijeuxolympiques.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role>findByName(ERole name);
}

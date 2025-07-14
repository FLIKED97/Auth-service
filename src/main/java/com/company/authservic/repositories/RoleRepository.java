package com.company.authservic.repositories;

import com.company.authservic.enums.ERole;
import com.company.authservic.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole eRole);
}

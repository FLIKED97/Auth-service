package com.company.authservic.models;

import com.company.authservic.enums.ERole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private ERole name;   // ENUM {ROLE_ADMIN, ROLE_MANAGER, ROLE_CLERK}
}

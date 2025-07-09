package com.company.authservic.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;            // логін користувача

    @Column(nullable = false, unique = true, length = 100)
    private String email;               // для нотифікацій, відновлення пароля

    @Column(nullable = false)
    private String password;            // захешований пароль (BCrypt)

    @Column(nullable = false, length = 50)
    private String firstName;           // ім’я

    @Column(nullable = false, length = 50)
    private String lastName;            // прізвище

    @Column(length = 20)
    private String phone;               // телефон (для SMS-сповіщень)

    @Column(nullable = false)
    private boolean enabled = true;     // активність обліковки

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;            // зв’язок із ролями (Admin, Manager, Clerk)

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdAt;          // дата створення

    @UpdateTimestamp
    private Instant updatedAt;          // дата останнього оновлення
}

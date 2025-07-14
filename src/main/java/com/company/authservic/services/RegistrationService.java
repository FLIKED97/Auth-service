package com.company.authservic.services;


import com.company.authservic.enums.ERole;
import com.company.authservic.models.Role;
import com.company.authservic.models.User;
import com.company.authservic.models.dto.UserRegistrationDTO;
import com.company.authservic.repositories.RoleRepository;
import com.company.authservic.repositories.UserRepository;
import com.company.authservic.security.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final UserRepository personRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public void register(User user) {

        // 1) Хешуємо пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2) Шукаємо роль
        Role clerkRole = roleRepository.findByName(ERole.ROLE_CLERK)
                .orElseThrow(() -> new RuntimeException("ROLE_CLERK not found"));

        // 3) Створюємо типізований Set і призначаємо
        Set<Role> roles = new HashSet<>();
        roles.add(clerkRole);
        user.setRoles(roles);

        // 4) Метадані
        user.setCreatedAt(Instant.now());

        // 5) Зберігаємо в БД
        personRepository.save(user);
    }
    public List<String> getUserRoles(String email) {
        return personRepository.findByEmail(email)
                .map(user -> user.getRoles().stream()
                        .map(role -> role.getName().name()) // або .toString()inventory
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Transactional
    public String registerAndGenerateToken(User user) {
        // 1) Хешуємо пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2) Шукаємо роль
        Role clerkRole = roleRepository.findByName(ERole.ROLE_CLERK)
                .orElseThrow(() -> new RuntimeException("ROLE_CLERK not found"));

        // 3) Створюємо типізований Set і призначаємо
        Set<Role> roles = new HashSet<>();
        roles.add(clerkRole);
        user.setRoles(roles);

        // 4) Метадані
        user.setCreatedAt(Instant.now());
        User saved = personRepository.save(user);

        return jwtUtil.generateToken(
                saved.getEmail(),
                saved.getRoles().stream()
                        .map(r -> r.getName().name())
                        .collect(Collectors.toList())
        );
    }

    public String loginAndGenerateToken(User user) {

        User existingUser = personRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(
                existingUser.getEmail(),
                existingUser.getRoles().stream()
                        .map(r -> r.getName().name())
                        .collect(Collectors.toList())
        );
    }
}
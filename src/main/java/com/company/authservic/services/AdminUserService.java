package com.company.authservic.services;

import com.company.authservic.enums.ERole;
import com.company.authservic.models.Role;
import com.company.authservic.models.User;
import com.company.authservic.models.dto.RolesDTO;
import com.company.authservic.models.dto.UserDTO;
import com.company.authservic.repositories.RoleRepository;
import com.company.authservic.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToUserDTO)
                .toList();
    }

    private UserDTO convertToUserDTO(User person){
        return modelMapper.map(person, UserDTO.class);
    }
    private User convertToUser(UserDTO personDTO){
        return modelMapper.map(personDTO, User.class);
    }

    public Optional<UserDTO> getUserById(Long id) {
        return Optional.ofNullable(convertToUserDTO(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"))));
    }

    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setUsername(dto.getUsername());

        userRepository.save(user);
        return convertToUserDTO(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Transactional
    public UserDTO updateRoles(Long id, RolesDTO rolesDto) {
        // 1. Знайти користувача
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        // 2. Перетворити кожний рядок у ERole → Role entity
        Set<Role> newRoles = rolesDto.getRoles().stream()
                .map(roleName -> {
                    // Переконаємось, що рядок відповідає одному з ENUM
                    ERole eRole = ERole.valueOf(roleName);
                    // Знайдемо сутність Role у БД
                    return roleRepository.findByName(eRole)
                            .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found"));
                })
                .collect(Collectors.toSet());

        // 3. Призначити новий сет ролей
        user.setRoles(newRoles);

        // 4. Зберегти оновленого користувача
        userRepository.save(user);

        // 5. Повернути DTO
        return convertToUserDTO(user);
    }

}

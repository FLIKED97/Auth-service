package com.company.authservic.controlers;

import com.company.authservic.models.dto.RolesDTO;
import com.company.authservic.models.dto.UserDTO;
import com.company.authservic.services.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;


    @GetMapping
    public List<UserDTO> listAllUsers() {
        return adminUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<UserDTO> getUserById(@PathVariable Long id) {
        return adminUserService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        return adminUserService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/roles")
    public UserDTO updateRoles(@PathVariable Long id, @RequestBody RolesDTO roles) {
        return adminUserService.updateRoles(id, roles);
    }
}

package com.company.authservic.controlers;

import com.company.authservic.models.dto.UserDTO;
import com.company.authservic.security.PersonDetails;
import com.company.authservic.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDTO getCurrentUser(@AuthenticationPrincipal PersonDetails userDetails) {
        return userService.getProfile(userDetails.getUsername());
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateUser(@AuthenticationPrincipal PersonDetails userDetails,
                                              @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(userDetails.getUsername(), userDTO));
    }
    @PutMapping("/me/password")
    public ResponseEntity<Map<String, String>> updatePassword(@AuthenticationPrincipal PersonDetails userDetails,
                                                              @RequestParam String oldPassword,
                                                              @RequestParam String newPassword) {
        userService.updatePassword(userDetails.getUsername(), oldPassword, newPassword);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));

    }
}

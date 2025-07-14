package com.company.authservic.controlers;

import com.company.authservic.models.User;
import com.company.authservic.models.dto.AuthenticationDTO;
import com.company.authservic.models.dto.UserRegistrationDTO;
import com.company.authservic.security.JWTUtil;
import com.company.authservic.services.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final RegistrationService registrationService;

    private final ModelMapper modelMapper;

    private final JWTUtil jwtUtil;

    @PostMapping("/registration")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegistrationDTO dto) {
        User user = convertToUser(dto);
        String token = registrationService.registerAndGenerateToken(user);
        return ResponseEntity.ok(Map.of("jwt-token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthenticationDTO dto) {
        User user = convertToUser(dto);
        String token = registrationService.loginAndGenerateToken(user);
        return ResponseEntity.ok(Map.of("jwt-token", token));
    }

    public User convertToUser(UserRegistrationDTO userRegistrationDTO) {
        return this.modelMapper.map(userRegistrationDTO, User.class);
    }
    public User convertToUser(AuthenticationDTO userRegistrationDTO) {
        return this.modelMapper.map(userRegistrationDTO, User.class);
    }
}

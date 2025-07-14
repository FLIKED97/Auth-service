package com.company.authservic.models.dto;


import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserDTO {
    private String username;            // логін користувача

    private String email;               // для нотифікацій, відновлення пароля

    private String firstName;           // ім’я

    private String lastName;            // прізвище

    private String phone;               // телефон (для SMS-сповіщень)
}

package com.company.authservic.models.dto;

import lombok.Data;

@Data
public class AuthenticationDTO {
    private String email;

    private String password;
}

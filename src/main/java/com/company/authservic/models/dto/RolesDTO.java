package com.company.authservic.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class RolesDTO {
    //@NotEmpty(message = "Roles list must not be empty")
    private List<String> roles;

    public RolesDTO() {}

    public RolesDTO(List<String> roles) {
        this.roles = roles;
    }

}

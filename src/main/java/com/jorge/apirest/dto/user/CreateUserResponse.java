package com.jorge.apirest.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jorge.apirest.dto.role.RoleDTO;
import lombok.Data;

import java.util.List;

@Data
public class CreateUserResponse {
    public Long id;
    public String name;
    public String lastName;
    public String email;
    public String phone;
    public String image;
    @JsonProperty("notification_token")
    public String notificationToken;
    List<RoleDTO> roles;
}

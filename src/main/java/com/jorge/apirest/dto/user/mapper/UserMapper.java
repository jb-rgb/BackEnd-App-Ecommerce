package com.jorge.apirest.dto.user.mapper;

import com.jorge.apirest.config.APIConfig;
import com.jorge.apirest.dto.role.RoleDTO;
import com.jorge.apirest.dto.user.UserResponse;
import com.jorge.apirest.models.Role;
import com.jorge.apirest.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user, List<Role> roles) {
        List<RoleDTO> roleDTOS = roles
                .stream()
                .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(), role.getRoute()))
                .toList();
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setRoles(roleDTOS);

        if (user.getImage() != null) {
            String imageUrl = APIConfig.BASE_URL + user.getImage();
            response.setImage(imageUrl);
        }

        return response;
    }
}

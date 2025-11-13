package com.jorge.apirest.models;

import com.jorge.apirest.models.id.UserRoleId;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_has_roles")
public class UserHasRoles {
    @EmbeddedId
    private UserRoleId id = new UserRoleId();
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    private User user;
    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "id_role")
    private Role role;
}

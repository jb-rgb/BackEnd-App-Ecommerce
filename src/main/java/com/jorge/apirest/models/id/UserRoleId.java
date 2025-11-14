package com.jorge.apirest.models.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class UserRoleId implements Serializable {
    @Column(name = "id_user")
    private Long userId;
    @Column(name = "id_role")
    private String roleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof  UserRoleId)) return false;
        UserRoleId userRoleId = (UserRoleId) o;
        return Objects.equals(userId, userRoleId.userId) && Objects.equals(roleId, userRoleId.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }

    public UserRoleId() {}

    public UserRoleId(Long userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}

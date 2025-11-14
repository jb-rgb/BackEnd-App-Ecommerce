package com.jorge.apirest.repositories;

import com.jorge.apirest.models.UserHasRoles;
import com.jorge.apirest.models.id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHasRolesRepository extends JpaRepository<UserHasRoles, UserRoleId> {
}

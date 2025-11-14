package com.jorge.apirest.services;

import com.jorge.apirest.dto.user.CreateUserRequest;
import com.jorge.apirest.models.Role;
import com.jorge.apirest.models.User;
import com.jorge.apirest.models.UserHasRoles;
import com.jorge.apirest.repositories.RoleRepository;
import com.jorge.apirest.repositories.UserHasRolesRepository;
import com.jorge.apirest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserHasRolesRepository userHasRolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setName(request.name);
        user.setLastName(request.lastName);
        user.setPhone(request.phone);
        user.setEmail(request.email);
        String encryptedPassword = passwordEncoder.encode(request.password);
        user.setPassword(encryptedPassword);
        User savedUser = userRepository.save(user);
        Role clientRole = roleRepository.findById("CLIENT").orElseThrow(
                () -> new RuntimeException("Role not found")
        );
        UserHasRoles userHasRoles = new UserHasRoles(savedUser, clientRole);
        userHasRolesRepository.save(userHasRoles);
        return savedUser;
    }
}

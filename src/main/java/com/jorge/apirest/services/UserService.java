package com.jorge.apirest.services;

import com.jorge.apirest.dto.role.RoleDTO;
import com.jorge.apirest.dto.user.CreateUserRequest;
import com.jorge.apirest.dto.user.CreateUserResponse;
import com.jorge.apirest.dto.user.LoginRequest;
import com.jorge.apirest.dto.user.LoginResponse;
import com.jorge.apirest.models.Role;
import com.jorge.apirest.models.User;
import com.jorge.apirest.models.UserHasRoles;
import com.jorge.apirest.repositories.RoleRepository;
import com.jorge.apirest.repositories.UserHasRolesRepository;
import com.jorge.apirest.repositories.UserRepository;
import com.jorge.apirest.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public CreateUserResponse create(CreateUserRequest request) {
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

        CreateUserResponse response = new CreateUserResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setLastName(savedUser.getLastName());
        response.setPhone(savedUser.getPhone());
        response.setEmail(savedUser.getEmail());
        response.setImage(savedUser.getImage());
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(savedUser.getId());
        List<RoleDTO> roleDTOS = roles
                .stream()
                .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(), role.getRoute()))
                .toList();
        response.setRoles(roleDTOS);

        return response;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new RuntimeException("The email address and password are not valid.")
        );
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            new RuntimeException("The email address and password are not valid.");
        }
        String token =jwtUtil.generateToken(user);
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());
        List<RoleDTO> roleDTOS = roles
                .stream()
                .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(), role.getRoute()))
                .toList();
        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setId(user.getId());
        createUserResponse.setName(user.getName());
        createUserResponse.setLastName(user.getLastName());
        createUserResponse.setPhone(user.getPhone());
        createUserResponse.setEmail(user.getEmail());
        createUserResponse.setImage(user.getImage());
        createUserResponse.setRoles(roleDTOS);
        LoginResponse response = new LoginResponse();
        response.setToken("Bearer " + token);
        response.setUser(createUserResponse);
        return response;
    }

    @Transactional
    public CreateUserResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("The user with id " + id + " does not exist.")
        );
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());
        List<RoleDTO> roleDTOS = roles
                .stream()
                .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(), role.getRoute()))
                .toList();
        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setId(user.getId());
        createUserResponse.setName(user.getName());
        createUserResponse.setLastName(user.getLastName());
        createUserResponse.setPhone(user.getPhone());
        createUserResponse.setEmail(user.getEmail());
        createUserResponse.setImage(user.getImage());
        createUserResponse.setRoles(roleDTOS);
        return createUserResponse;
    }
}

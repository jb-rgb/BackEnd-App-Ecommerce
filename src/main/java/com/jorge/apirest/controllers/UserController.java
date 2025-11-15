package com.jorge.apirest.controllers;

import com.jorge.apirest.dto.user.CreateUserRequest;
import com.jorge.apirest.dto.user.CreateUserResponse;
import com.jorge.apirest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request) {
        CreateUserResponse user = userService.create(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            CreateUserResponse response = userService.findById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "statusCode", HttpStatus.NOT_FOUND.value()
            ));
        }
    }
}

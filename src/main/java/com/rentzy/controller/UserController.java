package com.rentzy.controller;

import com.rentzy.enums.user.UserRole;
import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.exception.UserAlreadyExistsException;
import com.rentzy.model.dto.FilterParams;
import com.rentzy.model.dto.request.UserRequestDTO;
import com.rentzy.model.dto.response.UserResponseDTO;
import com.rentzy.service.UserService;
import com.rentzy.utils.PageableUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getUsers(
            @Valid @ModelAttribute FilterParams filterParams) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: " + authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority
                -> log.info(grantedAuthority.getAuthority()));

        Pageable pageable = PageableUtils.buildPageable(filterParams, 10);
        Page<UserResponseDTO> users = userService.getAllUsers(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO createdUser = userService.createUser(userRequestDTO);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdUser.getId())
                    .toUri();

            return ResponseEntity
                    .created(location)
                    .body(createdUser);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + id);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to delete this user.");
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<?> getUsersByRole(
            @PathVariable UserRole role,
            @Valid @ModelAttribute FilterParams filterParams) {
        Pageable pageable = PageableUtils.buildPageable(filterParams, 10);
        try {
            Page<UserResponseDTO> users = userService.getUsersByRole(role, pageable);
            return ResponseEntity.ok(users);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

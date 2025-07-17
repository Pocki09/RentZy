package com.rentzy.service;

import com.rentzy.enums.UserRole;
import com.rentzy.model.dto.request.UserRequestDTO;
import com.rentzy.model.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

    @PreAuthorize("hasRole('ADMIN')")
    Page<UserResponseDTO> getAllUsers(Pageable pageable);
    UserResponseDTO getUserById(String id);
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(String id, UserRequestDTO userRequestDTO);
    void deleteUser(String id);
    Page<UserResponseDTO> getUsersByRole(UserRole role, Pageable pageable);
}

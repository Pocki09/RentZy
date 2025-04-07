package com.rentzy.service;

import com.rentzy.enums.UserRole;
import com.rentzy.model.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<UserDTO> getAllUsers(Pageable pageable);
    UserDTO getUserById(String id);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(String id, UserDTO userDTO);
    void deleteUser(String id);
    Page<UserDTO> getUsersByRole(UserRole role, Pageable pageable);
    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);
}

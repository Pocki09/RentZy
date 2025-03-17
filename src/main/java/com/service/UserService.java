package com.service;

import com.enums.UserRole;
import com.model.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(String id);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(String id, UserDTO userDTO);
    void deleteUser(String id);
    List<UserDTO> getUsersByRole(UserRole role);
    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);
}

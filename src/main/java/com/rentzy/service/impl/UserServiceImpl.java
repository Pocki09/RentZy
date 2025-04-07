package com.rentzy.service.impl;

import com.rentzy.converter.UserConverter;
import com.rentzy.enums.UserRole;
import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.exception.UserAlreadyExistsException;
import com.rentzy.model.dto.UserDTO;
import com.rentzy.model.entity.UserEntity;
import com.rentzy.repository.UserRepository;
import com.rentzy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConverter userConverter;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        return userEntities.map(userConverter::toDTO);
    }

    @Override
    public UserDTO getUserById(String id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userConverter.toDTO(userEntity);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        UserEntity userEntity = userConverter.toEntity(userDTO);
        UserEntity savedUser = userRepository.save(userEntity);
        return userConverter.toDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(String id, UserDTO userDTO) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Kiểm tra username có bị trùng không
        if(!userEntity.getUsername().equals(userDTO.getUsername())
        && userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        // Kiểm tra email có bị trùng không
        if(!userEntity.getEmail().equals(userDTO.getEmail())
        && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setAvatar(userDTO.getAvatar());

        if (!userEntity.getUsername().equals(userDTO.getUsername())) {
            userEntity.setUsername(userDTO.getUsername());
        }
        if (!userEntity.getEmail().equals(userDTO.getEmail())) {
            userEntity.setEmail(userDTO.getEmail());
        }

        UserEntity result = userRepository.save(userEntity);
        return userConverter.toDTO(result);
    }

    @Override
    public void deleteUser(String id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (userEntity.getRole() == UserRole.ADMIN) {
            throw new IllegalStateException("Cannot delete an admin user");
        }

        userRepository.delete(userEntity);
    }

    @Override
    public Page<UserDTO> getUsersByRole(UserRole role, Pageable pageable) {
        Page<UserEntity> users = userRepository.findByRole(role, pageable);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found with role: " + role);
        }

        return users.map(userConverter::toDTO);
    }

    @Override
    public boolean isUsernameExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be null or empty");
        }
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return userRepository.existsByEmail(email);
    }
}

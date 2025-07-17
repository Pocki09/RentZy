package com.rentzy.service.impl;

import com.rentzy.converter.UserConverter;
import com.rentzy.enums.UserRole;
import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.exception.UserAlreadyExistsException;
import com.rentzy.model.dto.request.UserRequestDTO;
import com.rentzy.model.dto.response.UserResponseDTO;
import com.rentzy.entity.UserEntity;
import com.rentzy.repository.UserRepository;
import com.rentzy.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserConverter userConverter;
    PasswordEncoder passwordEncoder;

    static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        log.info("Get all users");
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        return userEntities.map(userConverter::toDTO);
    }

    @Override
    public UserResponseDTO getUserById(String id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userConverter.toDTO(userEntity);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        userRequestDTO.setRole(UserRole.USER);

        UserEntity userEntity = userConverter.toEntity(userRequestDTO);
        UserEntity savedUser = userRepository.save(userEntity);
        return userConverter.toDTO(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO userRequestDTO) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Kiểm tra username có bị trùng không
        if(!userEntity.getUsername().equals(userRequestDTO.getUsername())
        && userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        // Kiểm tra email có bị trùng không
        if(!userEntity.getEmail().equals(userRequestDTO.getEmail())
        && userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        userEntity.setUsername(userRequestDTO.getUsername());
        userEntity.setPhone(userRequestDTO.getPhone());
        userEntity.setAvatar(userRequestDTO.getAvatar());

        if (!userEntity.getUsername().equals(userRequestDTO.getUsername())) {
            userEntity.setUsername(userRequestDTO.getUsername());
        }
        if (!userEntity.getEmail().equals(userRequestDTO.getEmail())) {
            userEntity.setEmail(userRequestDTO.getEmail());
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
    public Page<UserResponseDTO> getUsersByRole(UserRole role, Pageable pageable) {
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

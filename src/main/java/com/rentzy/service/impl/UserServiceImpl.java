package com.rentzy.service.impl;

import com.rentzy.converter.UserConverter;
import com.rentzy.entity.UserEntity;
import com.rentzy.enums.user.UserRole;
import com.rentzy.enums.user.UserStatus;
import com.rentzy.model.dto.request.UserRequestDTO;
import com.rentzy.model.dto.response.UserResponseDTO;
import com.rentzy.repository.UserRepository;
import com.rentzy.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;


    @Override
    public UserDetails loadUserByUsername(String username) {
        return (UserDetails) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    @Transactional
    public UserResponseDTO registerStudent(UserRequestDTO user) {

        UserEntity userEntity = userConverter.toEntity(user);

        validateNewUser(userEntity);

        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setRole(UserRole.STUDENT);
        userEntity.setStatus(UserStatus.PENDING);
        userEntity.setEmailVerified(false);
        userEntity.setIdentityVerified(false);
        userEntity.setLoginAttempts(0);
        userEntity.setEmailVerificationToken(UUID.randomUUID().toString());
        userEntity.setEmailVerificationExpiry(Instant.now().plus(24, ChronoUnit.HOURS));

        UserEntity savedUser = userRepository.save(userEntity);

        log.info("Student registered: {} from university: {}",
                savedUser.getUsername(), savedUser.getUniversity());

        return userConverter.toDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO registerLandlord(UserRequestDTO user) {
        UserEntity userEntity = userConverter.toEntity(user);

        validateNewUser(userEntity);

        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setRole(UserRole.LANDLORD);
        userEntity.setStatus(UserStatus.PENDING);
        userEntity.setEmailVerified(false);
        userEntity.setIdentityVerified(false);
        userEntity.setLoginAttempts(0);
        userEntity.setEmailVerificationToken(UUID.randomUUID().toString());
        userEntity.setEmailVerificationExpiry(Instant.now().plus(24, ChronoUnit.HOURS));

        UserEntity savedUser = userRepository.save(userEntity);
        log.info("Landlord registered: {}", savedUser.getUsername());

        return userConverter.toDTO(savedUser);
    }

    @Override
    @Transactional
    public boolean verifyEmail(String token) {
        Optional<UserEntity> userOpt = userRepository.findByEmailVerificationToken(token);

        if (userOpt.isEmpty()) {
            return false;
        }

        UserEntity user = userOpt.get();

        if (user.getEmailVerificationExpiry().isBefore(Instant.now())) {
            return false; // Token expired
        }

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiry(null);

        // Auto-activate student sau khi verify email
        if (user.getRole() == UserRole.STUDENT) {
            user.setStatus(UserStatus.ACTIVE);
            log.info("Student activated: {}", user.getUsername());
        }

        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public void resendEmailVerification(String email) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty() || userOpt.get().isEmailVerified()) {
            return;
        }
        UserEntity user = userOpt.get();
        user.setEmailVerificationToken(UUID.randomUUID().toString());
        user.setEmailVerificationExpiry(Instant.now().plus(24, ChronoUnit.HOURS));

        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateProfile(String userId, UserRequestDTO updatedUser) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserEntity user = userOpt.get();

        user.setFullName(updatedUser.getFullName());
        user.setDateOfBirth(updatedUser.getDateOfBirth());
        user.setEmergencyContact(updatedUser.getEmergencyContact());
        user.setEmergencyPhone(updatedUser.getEmergencyPhone());

        if (user.getRole() == UserRole.STUDENT) {
            user.setUniversity(updatedUser.getUniversity());
            user.setStudentId(updatedUser.getStudentId());
            user.setMaxBudget(updatedUser.getMaxBudget());
        }

        if (updatedUser.getPhone() != null && !updatedUser.getPhone().equals(user.getPhone())) {
            if (userRepository.existsByPhone(updatedUser.getPhone())) {
                throw new RuntimeException("Phone number already exists");
            }
            user.setPhone(updatedUser.getPhone());
        }

        userRepository.save(user);
        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateAvatar(String userId, String avatarUrl) {

        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserEntity user = userOpt.get();
        user.setAvatar(avatarUrl);

        userRepository.save(user);
        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO submitIdentityVerification(String userId, String nationalId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        if (userRepository.existsByNationalId(nationalId)) {
            throw new RuntimeException("National ID already exists");
        }

        UserEntity user = userOpt.get();

        user.setNationalId(nationalId);
        user.setIdentityVerified(false);

        userRepository.save(user);
        log.info("Identity verification submitted for user: {}", user.getUsername());

        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO approveIdentityVerification(String userId) {

        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserEntity user = userOpt.get();
        user.setIdentityVerified(true);

        // Auto-activate landlord sau khi verify identity
        if (user.getRole() == UserRole.LANDLORD && user.isEmailVerified()) {
            user.setStatus(UserStatus.ACTIVE);
        }

        userRepository.save(user);
        log.info("Identity verified for user: {}", user.getUsername());

        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public void recordLoginAttempt(String username, boolean success) {
        Optional<UserEntity> userOpt = userRepository.findByUsernameOrEmail(username, username);

        if (userOpt.isEmpty()) {
            return;
        }

        UserEntity user = userOpt.get();

        if (success){
            user.setLoginAttempts(0);
            user.setLockoutUntil(null);
        } else {
            user.setLoginAttempts(user.getLoginAttempts() + 1);

            if (user.getLoginAttempts() >= 5){
                user.setLockoutUntil(Instant.now().plus(30, ChronoUnit.MINUTES));
                log.warn("User account locked due to failed login attempts: {}", username);
            }
        }

        userRepository.save(user);
    }

    @Override
    public List<UserEntity> findStudentsByUniversity(String university) {
        return userRepository.findByUniversityAndRoleOrderByCreatedAtDesc(university, UserRole.STUDENT);
    }

    @Override
    public Page<UserEntity> getUsersByRole(UserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }

    @Override
    public Page<UserEntity> getPendingVerifications(Pageable pageable) {
        return userRepository.findByIdentityVerifiedFalseAndStatusOrderByCreatedAtDesc(
                UserStatus.PENDING, pageable);
    }

    @Override
    public UserResponseDTO suspendUser(String userId, String reason) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserEntity user = userOpt.get();
        user.setStatus(UserStatus.SUSPENDED);

        userRepository.save(user);
        log.warn("User suspended: {} - Reason: {}", user.getUsername(), reason);

        return userConverter.toDTO(user);
    }

    @Override
    public UserResponseDTO reactivateUser(String userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserEntity user = userOpt.get();
        user.setStatus(UserStatus.ACTIVE);
        user.setLockoutUntil(null);
        user.setLoginAttempts(0);

        userRepository.save(user);
        return userConverter.toDTO(user);
    }

    @Override
    public Optional<UserEntity> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public long countUsersByRole(UserRole role) {
        return userRepository.countByRole(role);
    }

    @Override
    public long countUsersByStatus(UserStatus status) {
        return userRepository.countByStatus(status);
    }

    @Override
    public void cleanupUnverifiedUsers() {
        Instant cutoffDate = Instant.now().minus(30, ChronoUnit.DAYS);
        List<UserEntity> unverifiedUsers = userRepository.findUnverifiedUsersOlderThan(cutoffDate);

        userRepository.deleteAll(unverifiedUsers);

        log.info("Cleaned up {} unverified users", unverifiedUsers.size());
    }

    private void validateNewUser(UserEntity user){
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }
    }
}

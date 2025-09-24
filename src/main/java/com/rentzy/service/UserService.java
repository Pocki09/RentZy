package com.rentzy.service;

import com.rentzy.entity.UserEntity;
import com.rentzy.enums.user.UserRole;
import com.rentzy.enums.user.UserStatus;
import com.rentzy.model.dto.request.UserRequestDTO;
import com.rentzy.model.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDetails loadUserByUsername(String username);
    UserResponseDTO registerStudent(UserRequestDTO user);
    UserResponseDTO registerLandlord(UserRequestDTO user);

    // EMAIL VERIFICATION
    boolean verifyEmail(String token);
    void resendEmailVerification(String email);

    // PROFILE MANAGEMENT
    UserResponseDTO updateProfile(String userId, UserRequestDTO user);
    UserResponseDTO updateAvatar(String userId, String avatarUrl);

    // IDENTITY VERIFICATION (quan trọng cho trust & safety)
    UserResponseDTO submitIdentityVerification(String userId, String nationalId);
    UserResponseDTO approveIdentityVerification(String userId);

    // SECURITY
    void recordLoginAttempt(String username, boolean success);

    // STUDENT COMMUNITY FEATURES
    List<UserEntity> findStudentsByUniversity(String university);

    // ADMIN FUNCTIONS
    Page<UserEntity> getUsersByRole(UserRole role, Pageable pageable);
    Page<UserEntity> getPendingVerifications(Pageable pageable);
    UserResponseDTO suspendUser(String userId, String reason);
    UserResponseDTO reactivateUser(String userId);

    // UTILITY METHODS
    Optional<UserEntity> findById(String id);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    long countUsersByRole(UserRole role);
    long countUsersByStatus(UserStatus status);

    // CLEANUP TASKS
    void cleanupUnverifiedUsers();  // Xóa user chưa verify email sau 30 ngày
}

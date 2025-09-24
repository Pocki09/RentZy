package com.rentzy.model.dto.response;

import com.rentzy.enums.user.UserRole;
import com.rentzy.enums.user.UserStatus;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class UserResponseDTO {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private LocalDate dateOfBirth;
    private String phone;
    private String avatar;

    private UserRole role;
    private UserStatus status;

    // Student info
    private String university;
    private String studentId;
    private Double maxBudget;

    // Verification status
    private boolean emailVerified;
    private boolean identityVerified;

    private Instant createdAt;
}

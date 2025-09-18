package com.rentzy.entity;

import com.rentzy.enums.user.UserRole;
import com.rentzy.enums.user.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@Document(collection = "user")
public class UserEntity {
    @Id
    private String id;

    @NotBlank(message = "Username is required")
    @Indexed(unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Indexed(unique = true)
    private String email;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // ROLES & STATUS
    @Builder.Default
    private UserRole role = UserRole.STUDENT;
    @Builder.Default
    private UserStatus status = UserStatus.PENDING;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Indexed(unique = true, sparse = true) // sparse = true cho phép null
    private String phone;

    private String avatar;

    // STUDENT ESSENTIALS
    private String university;  // "ĐH Bách Khoa", "UEH", "UIT"...
    private String studentId;   // Mã số sinh viên để verify
    private Double maxBudget;

    // === VERIFICATION ===
    private boolean emailVerified;
    private String emailVerificationToken;
    private Instant emailVerificationExpiry;

    private boolean identityVerified; // Admin verify CCCD
    private String nationalId; // Số CCCD

    // SAFETY FOR STUDENT FAR FROM HOME
    private String emergencyContact;
    private String emergencyPhone;

    // SECURITY
    @Builder.Default
    private Integer loginAttempts = 0;
    private Instant lockoutUntil; // Khóa tài khoản đến thời điểm

    @CreatedDate
    private Instant  createdAt;
    @LastModifiedDate
    private Instant  updatedAt;
}

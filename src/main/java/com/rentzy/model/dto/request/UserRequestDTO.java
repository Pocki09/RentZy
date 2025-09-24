package com.rentzy.model.dto.request;

import com.rentzy.enums.user.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers and underscore")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private UserRole role;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "Invalid Vietnamese phone number")
    private String phone;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // Student-specific cho SV HCM
    @NotBlank(message = "University is required")
    private String university; // "ĐH Bách Khoa HCM", "UEH", "UIT"

    private String studentId; // Mã số sinh viên

    @NotBlank(message = "National ID is required")
    @Pattern(regexp = "^[0-9]{12}$", message = "National ID must be 12 digits")
    private String nationalId;

    @Positive(message = "Max budget must be positive")
    private Double maxBudget; // Ngân sách tối đa (VND)

    private String avatar;

    // Safety info (quan trọng với SV xa nhà)
    private String emergencyContact;
    private String emergencyPhone;
}

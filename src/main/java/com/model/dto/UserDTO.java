package com.model.dto;

import com.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {
    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username chỉ chứa tối đa 50 kí tự")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email chỉ chứa tối đa 100 kí tự")
    private String email;

    @NotNull(message = "Role là bắt buộc")
    private UserRole role;

    @NotBlank(message = "Full name là bắt buộc")
    @Size(max = 100, message = "Full name chỉ chứa tối đa 100 kí tự")
    private String fullName;

    @NotBlank(message = "Phone là bắt buộc")
    @Pattern(regexp = "^(\\+\\d{1,3})?\\d{10,12}$", message = "Phone phải hợp lệ")
    private String phone;

    private String avatar;
}

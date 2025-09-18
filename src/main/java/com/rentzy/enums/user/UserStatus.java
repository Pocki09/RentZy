package com.rentzy.enums.user;

import lombok.Getter;

@Getter
public enum UserStatus {
    PENDING("Chờ xác thực"),     // Mới đăng ký, chưa verify email
    ACTIVE("Hoạt động"),         // Đã verify, có thể sử dụng bình thường
    SUSPENDED("Tạm khóa"),
    BANNED("Cấm vĩnh viễn");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }
}

package com.rentzy.enums.user;

import lombok.Getter;

@Getter
public enum UserRole {
    STUDENT("Sinh viên"),
    LANDLORD("Chủ nhà"),
    ADMIN("Quản trị viên");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }
}

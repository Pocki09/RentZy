package com.rentzy.controller.auth.dto.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    String userId;
    String password;
}

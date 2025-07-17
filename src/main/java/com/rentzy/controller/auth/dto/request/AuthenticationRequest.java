package com.rentzy.controller.auth.dto.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    String username;
    String password;
}

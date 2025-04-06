package com.rentzy.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.time.Instant;

@Data
public class NotificationDTO {
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Message is required")
    private String message;

    @NotBlank(message = "Type is required")
    private String type;

    private Boolean isRead = false;
    private Instant createdAt = Instant.now();
}

package com.rentzy.model.dto.request;

import com.rentzy.enums.notification.NotificationPriority;
import com.rentzy.enums.notification.NotificationType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.time.Instant;
import java.util.Map;

@Data
public class NotificationRequestDTO {
    @NotBlank(message = "User ID is required")
    private String userId;

    private String relatedEntityId;
    private String relatedEntityType;

    private String title;
    @NotBlank(message = "Message is required")
    private String message;

    @NotBlank(message = "Type is required")
    private NotificationType type;
    @NotBlank(message = "priority is required")
    private NotificationPriority priority;

    private Map<String, Object> metadata;

    private Boolean requiresAction;
}

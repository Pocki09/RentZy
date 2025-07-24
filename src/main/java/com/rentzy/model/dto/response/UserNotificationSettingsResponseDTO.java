package com.rentzy.model.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class UserNotificationSettingsResponseDTO {
    private String id;
    private String userId;
    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean pushEnabled;
    private boolean inAppEnabled;
    private Map<String, Boolean> typePreferences; // "APPOINTMENT_CREATED" -> true/false
    private String timezone;
    private Integer quietHoursStart;
    private Integer quietHoursEnd;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 